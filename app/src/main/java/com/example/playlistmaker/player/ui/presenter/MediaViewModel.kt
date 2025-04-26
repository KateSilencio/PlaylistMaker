package com.example.playlistmaker.player.ui.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.domain.model.PlaylistMapper
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.mapper.TrackMapper
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.models.AddToPlaylistState
import com.example.playlistmaker.player.ui.models.MediaState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY_SEC = 300L
    }

    private var playerState = STATE_DEFAULT
    private var progressTimeJob: Job? = null

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    //_______________________________________________________________
    //для плеера:
    private val mediaState = MutableLiveData<MediaState>()
    val mediaStateLive: LiveData<MediaState> = mediaState

    //для плейлистов:
    // состояние добавления трека
    private val addToPlaylistState = MutableLiveData<AddToPlaylistState>()
    val addToPlaylistStateLive: LiveData<AddToPlaylistState> = addToPlaylistState

    //состояние BottomSheet:
    private val showPlaylistsState = MutableLiveData<Boolean>()
    val showToPlaylistStateLive: LiveData<Boolean> = showPlaylistsState

    //все загруженные плейлисты
    private val playlists = MutableLiveData<List<Playlist>>()
    val playlistsLive: LiveData<List<Playlist>> = playlists

    //*******************************************************************
    fun preparePlayer(track: Track) {
        mediaState.value = MediaState(track = track)
        mediaPlayerInteractor.prepareMedia(track.previewUrl)
        mediaPlayerInteractor.setOnPreparedListener {
            playerState = STATE_PREPARED
        }

        mediaPlayerInteractor.setOnCompletionListener {
            mediaState.value = mediaState.value?.copy(isPlaying = false, currentTime = "00:00")
            playerState = STATE_PREPARED
        }
    }

    fun playbackControl() {

        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun pausePlayer() {
        val currentState = mediaState.value ?: MediaState()
        mediaPlayerInteractor.pauseMedia()
        stopProgressTime()
        mediaState.value = currentState.copy(isPlaying = false)
        playerState = STATE_PAUSED
    }

    fun onFavoriteClicked() {
        val currentTrack = mediaState.value?.track ?: return
        viewModelScope.launch {
            //var updatedTrack = currentTrack.copy(isFavorite = currentTrack.isFavorite)
            if (currentTrack.isFavorite) {
                // Меняем состояние isFavorite
//                mediaState.value = mediaState.value
//                    ?.copy(track = currentTrack.copy(isFavorite = false))
                //updatedTrack = currentTrack.copy(isFavorite = false)
                favoriteTracksInteractor.removeTrackFromFavorites(
                    TrackMapper.convertToTracksData(
                        currentTrack.copy(isFavorite = false)
                    )
                )
            } else {
                // Меняем состояние isFavorite
//                mediaState.value = mediaState.value?.copy(track = currentTrack.copy(isFavorite = true))
                //updatedTrack = currentTrack.copy(isFavorite = true)
                favoriteTracksInteractor.addTrackToFavorites(
                    TrackMapper.convertToTracksData(
                        currentTrack.copy(isFavorite = true)
                    )
                )
            }
            // Меняем состояние isFavorite
            mediaState.value = mediaState.value
                ?.copy(track = currentTrack.copy(isFavorite = !currentTrack.isFavorite))
        }
    }

    private fun startPlayer() {
        val currentState = mediaState.value ?: MediaState()
        mediaPlayerInteractor.startMedia()
        mediaState.value = currentState.copy(isPlaying = true)
        playerState = STATE_PLAYING
        startUpdateTime()
    }

    private fun startUpdateTime() {
        progressTimeJob = viewModelScope.launch {
            while (playerState == STATE_PLAYING) {
                val currentPosition = mediaPlayerInteractor.getCurrentPosition()
                mediaState.value =
                    mediaState.value?.copy(currentTime = dateFormat.format(currentPosition))
                delay(DELAY_SEC)
            }
        }

    }

    fun releaseMedia() {
        mediaPlayerInteractor.releaseMedia()
    }

    override fun onCleared() {
        super.onCleared()
        releaseMedia()
    }

    private fun stopProgressTime() {
        progressTimeJob?.cancel()
    }

    //***********************************************************************
    //Логика для добавления трека в плейлист

    fun addToPlaylistClicked() {
        //при нажатии показываем
        showPlaylistsState.value = true
    }
    fun bottomSheetClosed() {
        //закрытие bottomSheet
        showPlaylistsState.value = false
    }

    suspend fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistInteractor.isTrackInPlaylist(track.trackId, playlist.id)
    }
    fun playlistSelected(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            val playlistEntity = PlaylistMapper.mapToEntity(playlist)
            val  isAdded = playlistInteractor.addTrackToPlaylist(track,playlistEntity)
            //скрываем bottomSheet
            //showPlaylistsState.value = false
            if (isAdded) {
                addToPlaylistState.value = AddToPlaylistState.Success(playlist.title)
            } else {
                addToPlaylistState.value = AddToPlaylistState.AlreadyAdded(playlist.title)
            }
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            try {
                playlistInteractor.getPlaylists().collect { _playlists ->
                    playlists.postValue(_playlists)
                    Log.d("AAA", "Playlists loaded MediaViewModel: ${playlists.value?.size}")
                }
            } catch (e: Exception) {
                playlists.postValue(emptyList())
            }
        }
    }
}