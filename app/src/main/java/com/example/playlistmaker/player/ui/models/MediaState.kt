package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.player.domain.models.Track

data class MediaState(
    val track: Track? = null,
    val isPlaying: Boolean = false,
    val currentTime: String = "00:00",
    var isFavorite: Boolean = false
)
