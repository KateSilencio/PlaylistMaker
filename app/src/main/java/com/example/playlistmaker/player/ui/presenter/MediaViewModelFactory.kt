package com.example.playlistmaker.player.ui.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor

class MediaViewModelFactory(private val mediaPlayerInteractor: MediaPlayerInteractor) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaViewModel(mediaPlayerInteractor) as T
    }
}