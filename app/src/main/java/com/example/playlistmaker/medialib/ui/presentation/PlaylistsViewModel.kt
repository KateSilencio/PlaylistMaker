package com.example.playlistmaker.medialib.ui.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistCreationState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,

): ViewModel() {

    //состояние плейлиста
    private val creationState = MutableLiveData<PlaylistCreationState>()
    val creationStateLiveData: LiveData<PlaylistCreationState> = creationState
    //состояние диалога
    private val showDialog = MutableLiveData<Boolean>()
    val showDiscardDialogLiveData: LiveData<Boolean> = showDialog

    var hasChanges = false

    private var playlistTitle: String = ""
    private var playlistDescription: String? = null
    private var playlistCoverUri: Uri? = null

    fun updatePlaylistData(
        title: String,
        description: String?,
        coverUri: Uri?
    ) {
        playlistTitle = title
        playlistDescription = description
        playlistCoverUri = coverUri

        hasChanges = title.isNotEmpty() || description?.isNotEmpty() == true || coverUri != null
    }

    //нажатие назад
    fun onBackPressed(){
        if (hasChanges ){
            showDialog.value = true
        } else {
            creationState.value = PlaylistCreationState.Canceled
        }
    }
    //Диалог: Завершить
    fun confirmDiscard(){
        showDialog.value = false
        creationState.value = PlaylistCreationState.Canceled
    }
    //Диалог: Отмена
    fun cancelDiscard(){
        showDialog.value = false
    }

    fun  savePlaylist(){
        creationState.value = PlaylistCreationState.Saving

        viewModelScope.launch {
            try {
                playlistInteractor.createPlaylist(
                    title = playlistTitle,
                    description = playlistDescription,
                    coverUri = playlistCoverUri
                )
                creationState.postValue(PlaylistCreationState.Success(playlistTitle))
            } catch (e: Exception){
                creationState.postValue(PlaylistCreationState.Canceled)
            }
        }
    }
}