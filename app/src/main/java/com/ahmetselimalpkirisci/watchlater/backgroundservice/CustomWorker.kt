// CustomWorker.kt
package com.ahmetselimalpkirisci.watchlater.backgroundservice

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmetselimalpkirisci.watchlater.db.UserRoomDatabase
import com.ahmetselimalpkirisci.watchlater.model.Recommendation
import com.ahmetselimalpkirisci.watchlater.model.User
import com.ahmetselimalpkirisci.watchlater.sys.MovieSys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Switch to IO dispatcher for database operations
            withContext(Dispatchers.IO) {
                val userDao = UserRoomDatabase.getDatabase(applicationContext).userDao()

                val allUsers: List<User> = userDao.getAllUsersList() // Suspend function

                // Process each user
                allUsers.forEach { user ->
                    // Generate recommendations
                    val recommendations = generateRecommendationsForUser(user)
                    // Update the user's recommendations
                    val updatedUser = user.copy(
                        recommendations = recommendations
                    )
                    userDao.updateUser(updatedUser)
                }
            }
            // Indicate successful completion
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            // Indicate failure
            Result.failure()
        }
    }

    /**
     * Generates a list of recommendations for a user based on their ratings.
     *
     * @param user The user for whom to generate recommendations.
     * @return A sorted list of recommendations ordered by highest points.
     */
    private fun generateRecommendationsForUser(user: User): List<Recommendation> {
        // Check if user has any ratings
        val userRatings = user.ratings ?: return emptyList()

        // Create a map for quick movie lookup by ID
        val movieMap = MovieSys.movies.associateBy { it.id }

        // Initialize a mutable map to accumulate points for each recommended movie
        val recommendationPoints = mutableMapOf<Int, Int>()

        // Iterate through each rating
        userRatings.forEach { rating ->
            // Get the rated movie from MovieSys
            val ratedMovie = movieMap[rating.movieId]
            if (ratedMovie != null) {
                // Get similar movie IDs; ensure it's not null
                val similarMovieIds = ratedMovie.similars ?: intArrayOf()

                similarMovieIds.forEach { similarId ->
                    // Skip if the user has already rated this similar movie
                    if (userRatings.none { it.movieId == similarId }) {
                        // Assign points based on the user's rating
                        val points = when (rating.point) {
                            in 0..10 -> rating.point
                            else -> 0
                        }

                        // Accumulate points for the similar movie
                        recommendationPoints[similarId] = (recommendationPoints[similarId] ?: 0) + points
                    }
                }
            }
        }

        // Sort the recommended movies by accumulated points in descending order
        val sortedRecommendations = recommendationPoints.entries
            .sortedByDescending { it.value }
            .mapNotNull { entry ->
                val movie = movieMap[entry.key]
                if (movie != null) {
                    Recommendation(
                        movieId = movie.id,
                        calcPoint = entry.value.toDouble(),
                        title = movie.title ?: "Unknown Title",
                        description = movie.synopsis ?: "No Description Available",
                        imageUrl = movie.imageUrl ?: ""
                    )
                } else {
                    null // Skip if movie details are unavailable
                }
            }

        return sortedRecommendations
    }
}
