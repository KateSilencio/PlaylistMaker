package com.example.playlistmaker.domain.api.media.usecase

interface MediaPlayerUseCase {

    fun prepareMedia(url: String)
    fun startMedia()
    fun pauseMedia()
    fun releaseMedia()
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun setOnPreparedListener(listener: () -> Unit)
}