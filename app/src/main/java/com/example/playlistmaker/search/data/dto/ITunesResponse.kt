package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.player.domain.models.TracksData
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<TracksData>
): Response()

