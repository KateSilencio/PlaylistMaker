package com.example.playlistmaker.medialib.ui.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.domain.model.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist
) : ViewModel() {

    private val saveResult = MutableLiveData<Boolean>()
    val saveResultLiveData: LiveData<Boolean> = saveResult

    fun savePlaylist(title: String, description: String?, coverUri: Uri?) {
        if (title.isEmpty()) {
            saveResult.postValue(false)
            return
        }

        viewModelScope.launch {
            try {
                playlistInteractor.updatePlaylist(
                    playlistId = playlist.id,
                    title = title,
                    description = description,
                    coverUri = coverUri
                )
                saveResult.postValue(true)
            } catch (e: Exception) {
                saveResult.postValue(false)
            }
        }
    }

//    fun savePlaylist(title: String, description: String?, coverUri: Uri?) {
//        if (title.isEmpty()) return
//
//        viewModelScope.launch {
//            playlistInteractor.updatePlaylist(
//                playlistId = playlist.id,
//                title = title,
//                description = description,
//                coverUri = coverUri
//            )
//        }
//    }
}