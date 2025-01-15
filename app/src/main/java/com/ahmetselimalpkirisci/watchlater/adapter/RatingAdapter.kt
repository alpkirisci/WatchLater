// RatingAdapter.kt
package com.ahmetselimalpkirisci.watchlater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.ahmetselimalpkirisci.watchlater.databinding.ItemRatingCardBinding
import com.ahmetselimalpkirisci.watchlater.model.Movie
import com.yuyakaido.android.cardstackview.*
import com.bumptech.glide.Glide

class RatingAdapter(
    private val cardStackView: CardStackView
) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    private val movies = mutableListOf<Movie>()
    private val ratingsMap = mutableMapOf<Int, Int>() // Key: Position, Value: Rating

    fun setMovies(movieList: List<Movie>) {
        movies.clear()
        movies.addAll(movieList)
        notifyDataSetChanged()
    }

    fun getMovieAt(position: Int): Movie = movies[position]

    fun getRatingAt(position: Int): Int = ratingsMap[position] ?: 0

    inner class RatingViewHolder(private val binding: ItemRatingCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.txtMovieTitle.text = movie.title
            binding.txtSynopsis.text = movie.synopsis

            // Load image with Glide
            Glide.with(binding.imgMovie.context)
                .load(movie.imageUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.imgMovie)

            // Initialize RatingBar with existing rating if available
            binding.ratingBar.rating = ratingsMap[adapterPosition]?.toFloat() ?: 0f

            // Capture rating changes
            binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                ratingsMap[bindingAdapterPosition] = rating.toInt()
            }

            // Handle "Didn't Watch It" button
            binding.btnNotWatched.setOnClickListener {
                val manager = cardStackView.layoutManager as? CardStackLayoutManager
                manager?.let {
                    val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                    it.setSwipeAnimationSetting(setting)
                    cardStackView.swipe()
                }
            }

            // Handle "Rate It" button
            binding.btnRate.setOnClickListener {
                val manager = cardStackView.layoutManager as? CardStackLayoutManager
                val rating = binding.ratingBar.rating.toInt()
                ratingsMap[bindingAdapterPosition] = rating

                manager?.let {
                    val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                    it.setSwipeAnimationSetting(setting)
                    cardStackView.swipe()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = ItemRatingCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    // In RatingAdapter.kt

    fun removeMovieAt(position: Int) {
        if (position >= 0 && position < movies.size) {
            movies.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int = movies.size
}
