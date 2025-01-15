// RecommendationsAdapter.kt
package com.ahmetselimalpkirisci.watchlater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetselimalpkirisci.watchlater.databinding.ItemRecommendationEvenBinding
import com.ahmetselimalpkirisci.watchlater.databinding.ItemRecommendationOddBinding
import com.ahmetselimalpkirisci.watchlater.model.Recommendation
import com.ahmetselimalpkirisci.watchlater.sys.MovieSys
import com.bumptech.glide.Glide

class RecommendationsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recommendations = mutableListOf<Recommendation>()

    companion object {
        private const val VIEW_TYPE_EVEN = 0
        private const val VIEW_TYPE_ODD = 1
    }

    /**
     * Sets the list of recommendations and notifies the adapter.
     *
     * @param recommendationList The list of recommendations to display.
     */
    fun setRecommendations(recommendationList: List<Recommendation>) {
        recommendations.clear()
        recommendations.addAll(recommendationList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_EVEN else VIEW_TYPE_ODD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_EVEN) {
            val binding = ItemRecommendationEvenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            EvenViewHolder(binding)
        } else {
            val binding = ItemRecommendationOddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OddViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recommendation = recommendations[position]
        if (holder is EvenViewHolder) {
            holder.bind(recommendation)
        } else if (holder is OddViewHolder) {
            holder.bind(recommendation)
        }
    }

    override fun getItemCount(): Int = recommendations.size

    /**
     * ViewHolder for even-positioned recommendation items.
     */
    inner class EvenViewHolder(private val binding: ItemRecommendationEvenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: Recommendation) {
            // Fetch the movie details using movieId
            val movie = MovieSys.movies.find { it.id == recommendation.movieId }
            if (movie != null) {
                binding.txtTitleEven.text = movie.title
                binding.txtDescriptionEven.text = movie.synopsis
                binding.txtPointsEven.text = "Points: ${recommendation.calcPoint}"

                // Load image using Glide
                Glide.with(binding.imgMovieEven.context)
                    .load(movie.imageUrl)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(binding.imgMovieEven)
            } else {
                // Handle missing movie details
                binding.txtTitleEven.text = "Unknown Title"
                binding.txtDescriptionEven.text = "No Description Available"
                binding.txtPointsEven.text = "Points: ${recommendation.calcPoint}"
                binding.imgMovieEven.setImageResource(R.drawable.ic_movie_placeholder)
            }
        }
    }

    /**
     * ViewHolder for odd-positioned recommendation items.
     */
    inner class OddViewHolder(private val binding: ItemRecommendationOddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: Recommendation) {
            // Fetch the movie details using movieId
            val movie = MovieSys.movies.find { it.id == recommendation.movieId }
            if (movie != null) {
                binding.txtTitleOdd.text = movie.title
                binding.txtDescriptionOdd.text = movie.synopsis
                binding.txtPointsOdd.text = "Points: ${recommendation.calcPoint}"

                // Load image using Glide
                Glide.with(binding.imgMovieOdd.context)
                    .load(movie.imageUrl)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(binding.imgMovieOdd)
            } else {
                // Handle missing movie details
                binding.txtTitleOdd.text = "Unknown Title"
                binding.txtDescriptionOdd.text = "No Description Available"
                binding.txtPointsOdd.text = "Points: ${recommendation.calcPoint}"
                binding.imgMovieOdd.setImageResource(R.drawable.ic_movie_placeholder)
            }
        }
    }
}
