package com.example.playlistmaker.player.ui.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.models.MediaState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY_SEC = 300L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var playerState = STATE_DEFAULT

    //потоки
    //private lateinit var runnable: Runnable
    //val handler = Handler(Looper.getMainLooper())

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
            //stopProgressTime()
//            if (::runnable.isInitialized) {
//                handler.removeCallbacks(runnable)
//            }

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
//        if (::runnable.isInitialized) {
//            handler.removeCallbacks(runnable)
//        }
        mediaState.value = currentState.copy(isPlaying = false)
        playerState = STATE_PAUSED
    }

    private fun startPlayer() {
        val currentState = mediaState.value ?: MediaState()
        mediaPlayerInteractor.startMedia()
        mediaState.value = currentState.copy(isPlaying = true)
        playerState = STATE_PLAYING
        startUpdateTime()
    }

    private fun startUpdateTime() {
//        if (::runnable.isInitialized) {
//            handler.removeCallbacks(runnable)
//        }
//        runnable = object : Runnable {
//            override fun run() {
//                if (playerState == STATE_PLAYING) {
//                    val currentPosition = mediaPlayerInteractor.getCurrentPosition()
//                    mediaState.value =
//                        mediaState.value?.copy(currentTime = dateFormat.format(currentPosition))
//                    handler.postDelayed(this, DELAY_SEC)
//                }
//            }
//        }
//        handler.post(runnable)

        //progressTimeJob?.cancel()
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
//        if (::runnable.isInitialized) {
//            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
//        }
        //stopProgressTime()
        releaseMedia()
    }

    private fun stopProgressTime(){
        progressTimeJob?.cancel()
    }

}