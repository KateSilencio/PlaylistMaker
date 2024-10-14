package com.example.playlistmaker.data.media

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.media.MediaPlayerRepository

class MediaPlayerRepositoryImpl: MediaPlayerRepository {

    private val mediaPlayer = MediaPlayer()
    override fun onPrepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun onStart() {
        mediaPlayer.start()
    }

    override fun onPause() {
        mediaPlayer.pause()
    }

    override fun onRelease() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            listener()
        }
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            listener()
        }
    }
}