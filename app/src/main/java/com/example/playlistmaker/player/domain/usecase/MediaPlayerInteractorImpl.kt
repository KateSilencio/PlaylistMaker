package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository):
    MediaPlayerInteractor {

    override fun prepareMedia(url: String) {
        mediaPlayerRepository.onPrepare(url)
    }

    override fun startMedia() {
        mediaPlayerRepository.onStart()
    }

    override fun pauseMedia() {
        mediaPlayerRepository.onPause()
    }

    override fun releaseMedia() {
        mediaPlayerRepository.onRelease()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(listener)
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnPreparedListener(listener)
    }
}