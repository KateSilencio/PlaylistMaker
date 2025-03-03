package com.example.playlistmaker.medialib.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.medialib.ui.presentation.models.FavoriteTracksState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor): ViewModel() {

    private val favoriteState = MutableLiveData<FavoriteTracksState>()
    val favoriteStateLive: LiveData<FavoriteTracksState> = favoriteState

    init {
        loadFavoriteTracks()
    }

    private fun loadFavoriteTracks(){
        favoriteState.value = FavoriteTracksState.Loading
        viewModelScope.launch {
            favoriteTracksInteractor.getAllFavoriteTracks().collect{ tracks ->
                if(tracks.isEmpty()){
                    favoriteState.value = FavoriteTracksState.NoData
                } else {
                    favoriteState.value = FavoriteTracksState.LoadedTracks(tracks)
                }
            }
        }
    }
}