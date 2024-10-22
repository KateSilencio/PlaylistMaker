package com.example.playlistmaker.player.domain

interface MediaPlayerRepository {

    fun onPrepare(url: String)
    fun onStart()
    fun onPause()
    fun onRelease()
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun setOnPreparedListener(listener: () -> Unit)


}