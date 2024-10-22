package com.example.playlistmaker.player.domain.usecase

interface MediaPlayerInteractor {

    fun prepareMedia(url: String)
    fun startMedia()
    fun pauseMedia()
    fun releaseMedia()
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun setOnPreparedListener(listener: () -> Unit)
}