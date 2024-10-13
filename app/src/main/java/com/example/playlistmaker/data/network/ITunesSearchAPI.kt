package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search")
    fun search(@Query("entity") entity: String,
               @Query("term") text: String): Call<ITunesResponse>

}