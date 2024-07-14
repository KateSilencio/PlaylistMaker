package com.example.playlistmaker.data

import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<ResultFields>
)
data class ResultFields(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Int,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String
)
