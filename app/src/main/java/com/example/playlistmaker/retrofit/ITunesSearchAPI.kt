package com.example.playlistmaker.retrofit

import com.example.playlistmaker.data.ITunesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search")
    fun search(@Query("entity") entity: String,
               @Query("term") text: String): Call<ITunesResponse>

    companion object{

        private val baseUrl = "https://itunes.apple.com"

        private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ITunesApiCreate = retrofit.create(ITunesSearchAPI::class.java)

    }
}