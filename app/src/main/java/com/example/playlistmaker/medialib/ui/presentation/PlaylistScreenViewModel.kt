package com.example.playlistmaker.medialib.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.domain.api.shareplaylist.PlaylistShareInteractor
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistShareState
import com.example.playlistmaker.player.domain.mapper.TrackMapper
import com.example.playlistmaker.player.domain.models.TracksData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistScreenViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistShareInteractor: PlaylistShareInteractor
) : ViewModel() {

    private val playlistScr = MutableLiveData<Playlist>()
    val playlistScrLive: LiveData<Playlist> = playlistScr

    //длительность треков:
    private val _duration = MutableLiveData<Int>()
    val durationLive: LiveData<Int> = _duration

    //список треков
    private val _tracks = MutableLiveData<List<TracksData>>()
    val tracksLive: LiveData<List<TracksData>> = _tracks

    //Состояния Поделиться
    private val _shareState = MutableLiveData<PlaylistShareState>()
    val shareStateLive: LiveData<PlaylistShareState> = _shareState

    fun loadPlaylistData(playlistId: Long) {
        viewModelScope.launch {

                //загружаем данные плейлиста
                playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                    playlist?.let {
                        playlistScr.postValue(it)
                        //длительность треков
                        val playlistEntity = playlistInteractor.getPlaylistEntityById(playlistId)
                        playlistEntity?.let { entity ->
                            val durationMs =
                                playlistInteractor.getPlaylistDuration(entity.getTrackIds())
                            _duration.postValue(formatDuration(durationMs))

                            //треки:
                            val tracks = playlistInteractor.getTracksForPlaylist(entity.getTrackIds())

                            val tracksData = tracks.map { track ->
                                TrackMapper.convertToTracksData(track)
                            }
                            _tracks.postValue(tracksData)
                        }
                    }
                }
        }
    }

    fun deleteTrack(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(playlistId, trackId.toLong())
            // после удаления обновляем данные
            loadPlaylistData(playlistId)
        }
    }

    private fun formatDuration(durationMs: Long): Int {
        val date = Date(durationMs)
        return SimpleDateFormat("mm", Locale.getDefault()).format(date).toInt()
    }

    fun prepareShareContent() {
        //загружаем данные из др LiveData
        val playlist = playlistScrLive.value
        val tracks = tracksLive.value ?: emptyList()

        if (tracks.isEmpty()) {
            _shareState.value = PlaylistShareState.Empty
        } else {
            playlist?.let {
                val shareText = playlistShareInteractor.getShareText(it, tracks)
                _shareState.value = PlaylistShareState.WithTracks(shareText)
            }
        }
    }

    fun executeShare() {
        when (val currentState = _shareState.value) {
            is PlaylistShareState.WithTracks -> {
                playlistShareInteractor.onSharePlaylist(currentState.shareText)
            }
            else -> {}
        }
    }

}