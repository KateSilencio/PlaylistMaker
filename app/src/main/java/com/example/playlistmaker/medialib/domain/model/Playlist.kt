package com.example.playlistmaker.medialib.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val title: String,
    val description: String?,
    val coverUri: String?,
    val trackCount: Int
): Parcelable
