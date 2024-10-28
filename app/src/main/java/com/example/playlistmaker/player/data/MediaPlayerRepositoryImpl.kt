package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer = MediaPlayer()):
    MediaPlayerRepository {

    //private val mediaPlayer = MediaPlayer()
    override fun onPrepare(url: String) {
        //сброс состояния
        mediaPlayer.reset()
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