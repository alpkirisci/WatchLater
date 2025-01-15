// RatingActivity.kt
package com.ahmetselimalpkirisci.watchlater

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import androidx.work.*
import com.ahmetselimalpkirisci.watchlater.backgroundservice.CustomWorker
import com.ahmetselimalpkirisci.watchlater.databinding.ActivityRatingBinding
import com.ahmetselimalpkirisci.watchlater.db.DBViewModel
import com.ahmetselimalpkirisci.watchlater.model.Rating
import com.ahmetselimalpkirisci.watchlater.model.User
import com.ahmetselimalpkirisci.watchlater.retrofit.ApiClient
import com.ahmetselimalpkirisci.watchlater.sys.MovieSys
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: RatingAdapter

    private val dbViewModel: DBViewModel by viewModels()
    private var currentUser: User? = null
    private var moviesLoaded = false // Flag to ensure movies are loaded only once

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCardStackView()
        observeUser()
        // setupWorker() // Not needed here since we'll enqueue when all movies are swiped
    }

    private fun setupCardStackView() {
        manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {}

            override fun onCardSwiped(direction: Direction) {
                val swipedPosition = manager.topPosition - 1
                if (swipedPosition >= 0 && swipedPosition < adapter.itemCount) {
                    val movie = adapter.getMovieAt(swipedPosition)
                    when (direction) {
                        Direction.Right -> {
                            val rating = adapter.getRatingAt(swipedPosition)
                            Toast.makeText(
                                this@RatingActivity,
                                "Rated: $rating for ${movie.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Save the rating to the database
                            saveRating(movie.id, rating)
                        }
                        Direction.Left -> {
                            Toast.makeText(
                                this@RatingActivity,
                                "Passed: ${movie.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Optionally handle pass logic
                        }
                        else -> {}
                    }

                    // Remove the swiped movie from the adapter
                    adapter.removeMovieAt(swipedPosition)

                    // Check if adapter is now empty after swiping
                    if (adapter.itemCount == 0) {
                        // All movies have been rated or passed
                        enqueueRecommendationWorker()
                    }
                }
            }

            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View, position: Int) {}
            override fun onCardDisappeared(view: View, position: Int) {}
        })

        binding.cardStackView.layoutManager = manager
        adapter = RatingAdapter(binding.cardStackView)
        binding.cardStackView.adapter = adapter

        // Customize CardStackLayoutManager settings
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
    }

    private fun observeUser() {
        dbViewModel.readAllDataUser.observe(this, Observer { users ->
            if (users.isNotEmpty() && !moviesLoaded) { // Check the flag
                currentUser = users[0] // Assuming single user
                loadMovies()
                moviesLoaded = true // Set the flag to prevent reloading
            }
        })
    }

    private fun loadMovies() {
        lifecycleScope.launch {
            try {
                // Fetch movies from API
                val fetchedMovies = ApiClient.apiService.getMovies()
                MovieSys.movies = fetchedMovies

                // Filter out already rated movies
                val ratedMovieIds = currentUser?.ratings?.map { it.movieId } ?: emptyList()
                val filteredMovies = fetchedMovies.filter { it.id !in ratedMovieIds }

                // Set movies to adapter
                adapter.setMovies(filteredMovies)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@RatingActivity, "Failed to load movies", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveRating(movieId: Int, ratingValue: Int) {
        currentUser?.let { user ->
            val newRating = Rating(movieId = movieId, point = ratingValue)
            val updatedUser = user.copy(
                ratings = user.ratings?.plus(newRating) ?: listOf(newRating)
            )
            dbViewModel.updateUser(updatedUser)
        }
    }

    private fun enqueueRecommendationWorker() {
        // Define constraints if any (e.g., require network)
        val constraints = Constraints.Builder()
            .build()

        // Create the worker request (One-time WorkRequest)
        val workerRequest = OneTimeWorkRequestBuilder<CustomWorker>()
            .setConstraints(constraints)
            .build()

        // Enqueue the worker
        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork(
                "GenerateRecommendations",
                ExistingWorkPolicy.REPLACE,
                workerRequest
            )

        // Observe the worker's status
        WorkManager.getInstance(applicationContext)
            .getWorkInfoByIdLiveData(workerRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            // Recommendations generated successfully, navigate to RecommendationsActivity
                            navigateToRecommendations()
                        }
                        WorkInfo.State.FAILED -> {
                            // Handle failure
                            Toast.makeText(
                                this,
                                "Failed to generate recommendations",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            // Handle other states if necessary
                        }
                    }
                }
            })
    }

    private fun navigateToRecommendations() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish RatingActivity so the user cannot return to it
    }
}
