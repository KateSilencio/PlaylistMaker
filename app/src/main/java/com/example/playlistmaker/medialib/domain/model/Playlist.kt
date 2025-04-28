package com.example.playlistmaker.medialib.domain.model

data class Playlist(
    val id: Long,
    val title: String,
    val coverUri: String?,
    val trackCount: Int
)
