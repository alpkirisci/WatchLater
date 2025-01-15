package com.ahmetselimalpkirisci.watchlater.backgroundservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.ahmetselimalpkirisci.watchlater.R

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        // Initialize MediaPlayer with the MP3 file
        mediaPlayer = MediaPlayer.create(this, R.raw.drive_it_like_you_stole_it)
        mediaPlayer.isLooping = true // Loop the music
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start() // Start playback
        return START_STICKY // Keep the service running
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop() // Stop playback
        mediaPlayer.release() // Release resources
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
