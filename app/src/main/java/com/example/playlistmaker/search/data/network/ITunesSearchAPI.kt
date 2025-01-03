package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search")
    suspend fun search(@Query("entity") entity: String,
               @Query("term") text: String): ITunesResponse

}