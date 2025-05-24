package com.example.playlistmaker.medialib.domain.impl.shareplaylist

import com.example.playlistmaker.medialib.domain.api.shareplaylist.PlaylistShareInteractor
import com.example.playlistmaker.medialib.domain.api.shareplaylist.PlaylistShareNavigationRepository
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.player.domain.models.TracksData
import java.util.Locale

class PlaylistShareInteractorImpl(
    private val playlistShareNavigationRepository: PlaylistShareNavigationRepository
): PlaylistShareInteractor {
    override fun getShareText(playlist: Playlist, tracks: List<TracksData>): String {
        //создаем строку
        return buildString {
            // название плейлиста
            append(playlist.title)
            append("\n")
            //описание если есть
            playlist.description?.let { desc ->
                append(desc)
                append("\n")
            }
            //количество треков
            append("[${tracks.size}] ${getTracksForm(tracks.size)}")
            append("\n\n")
            //каждый трек
            tracks.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${formatTrackDuration(track.trackTimeMillis)})")
                append("\n")
            }
        }
    }
    //из миллисек в ММ:СС
    private fun formatTrackDuration(durationMs: Int): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    //склонение трека
    private fun getTracksForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }
    override fun onSharePlaylist(text: String){
        return playlistShareNavigationRepository.sharePlaylist(text)
    }

}