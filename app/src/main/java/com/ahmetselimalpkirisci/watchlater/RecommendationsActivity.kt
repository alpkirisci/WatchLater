// RecommendationsActivity.kt
package com.ahmetselimalpkirisci.watchlater

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetselimalpkirisci.watchlater.databinding.ActivityRecommendationsBinding
import com.ahmetselimalpkirisci.watchlater.db.DBViewModel
import com.ahmetselimalpkirisci.watchlater.model.Recommendation

class RecommendationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationsBinding
    private lateinit var adapter: RecommendationsAdapter

    private val dbViewModel: DBViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeRecommendations()
    }

    /**
     * Sets up the RecyclerView with the RecommendationsAdapter.
     */
    private fun setupRecyclerView() {
        adapter = RecommendationsAdapter()
        binding.recyclerViewRecommendations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecommendations.adapter = adapter
    }

    /**
     * Observes the user's recommendations from the database and updates the adapter.
     */
    private fun observeRecommendations() {
        dbViewModel.readAllDataUser.observe(this, Observer { users ->
            if (users.isNotEmpty()) {
                val user = users[0] // Assuming single user
                val recommendations = user.recommendations
                if (!recommendations.isNullOrEmpty()) {
                    adapter.setRecommendations(recommendations)
                } else {
                    Toast.makeText(this, "No recommendations available", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
