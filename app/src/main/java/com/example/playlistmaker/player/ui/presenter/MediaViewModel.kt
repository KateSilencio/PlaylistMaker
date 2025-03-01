package com.example.playlistmaker.player.ui.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.mapper.TrackMapper
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.models.MediaState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY_SEC = 300L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var playerState = STATE_DEFAULT

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val mediaState = MutableLiveData<MediaState>()
    val mediaStateLive: LiveData<MediaState> = mediaState

    private var progressTimeJob: Job? = null

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

    fun onFavoriteClicked(){
        val currentTrack = mediaState.value?.track?: return
        viewModelScope.launch {
            //var updatedTrack = currentTrack.copy(isFavorite = currentTrack.isFavorite)
            if (currentTrack.isFavorite) {
                // Меняем состояние isFavorite
//                mediaState.value = mediaState.value
//                    ?.copy(track = currentTrack.copy(isFavorite = false))
                //updatedTrack = currentTrack.copy(isFavorite = false)
                favoriteTracksInteractor.removeTrackFromFavorites(TrackMapper.convertToTracksData(currentTrack.copy(isFavorite = false)))
            } else {
                // Меняем состояние isFavorite
//                mediaState.value = mediaState.value?.copy(track = currentTrack.copy(isFavorite = true))
                //updatedTrack = currentTrack.copy(isFavorite = true)
                favoriteTracksInteractor.addTrackToFavorites(TrackMapper.convertToTracksData(currentTrack.copy(isFavorite = true)))
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
            while(playerState == STATE_PLAYING){
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

    private fun stopProgressTime(){
        progressTimeJob?.cancel()
    }
}