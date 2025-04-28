package com.example.playlistmaker.medialib.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistShowViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val _showPlaylistLiveData = MutableLiveData<PlaylistState>()
    val showPlaylistLiveData: LiveData<PlaylistState> = _showPlaylistLiveData

    fun loadPlaylists() {
        viewModelScope.launch {
            try {
                playlistInteractor.getPlaylists().collect(){ playlists ->
                    _showPlaylistLiveData.postValue(
                        if (playlists.isEmpty()) PlaylistState.Empty
                        else PlaylistState.LoadedPlaylist(playlists)
                    )
                }
            } catch (e: Exception){
                _showPlaylistLiveData.postValue(PlaylistState.Error)
            }
        }
    }
}