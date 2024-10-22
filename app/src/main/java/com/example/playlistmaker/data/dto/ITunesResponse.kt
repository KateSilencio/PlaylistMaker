package com.example.playlistmaker.data.dto

import com.example.playlistmaker.player.domain.models.TracksParceling
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<TracksParceling>
): Response()

