package com.example.playlistmaker.medialib.domain.model
import com.example.playlistmaker.medialib.data.model.PlaylistEntity

object PlaylistMapper {
    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.playlistId,
            title = playlistEntity.title,
            coverUri = playlistEntity.coverPath,
            trackCount = playlistEntity.trackCount
        )
    }

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.id,
            title = playlist.title,
            description = null,
            coverPath = playlist.coverUri,
            tracksJson = "[]",
            trackCount = playlist.trackCount
        )
    }
}