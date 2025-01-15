package com.ahmetselimalpkirisci.watchlater

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.ahmetselimalpkirisci.watchlater.backgroundservice.MusicService
import com.ahmetselimalpkirisci.watchlater.databinding.ActivityMainBinding
import com.ahmetselimalpkirisci.watchlater.db.DBViewModel
import com.ahmetselimalpkirisci.watchlater.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private val dbViewModel: DBViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install and set up the splash screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val musicServiceIntent = Intent(this, MusicService::class.java)
        startService(musicServiceIntent)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        // Keep splash screen visible until data is loaded
        var isDataLoaded = false
        splashScreen.setKeepOnScreenCondition { !isDataLoaded }

        // Observe the user data
        dbViewModel.readAllDataUser.observe(this, Observer { users ->
            if (users.isNotEmpty()) {
                // User exists, navigate to RatingActivity
                navigateToRating()
            }
            // Data loaded, hide splash
            isDataLoaded = true
        })

        // Set up Continue button click listener
        bindingMain.btnContinue.setOnClickListener {
            val username = bindingMain.etUsername.text?.toString()?.trim()
            if (!username.isNullOrEmpty()) {
                // Create a new User with empty lists
                val newUser = User(
                    username = username,
                    ratings = emptyList(),
                    recommendations = emptyList()
                )
                dbViewModel.addUser(newUser)
                navigateToRating()
            } else {
                // Show error message
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToRating() {
        val intent = Intent(this, RatingActivity::class.java)
        startActivity(intent)
        finish() // Optional: close MainActivity
    }
}
