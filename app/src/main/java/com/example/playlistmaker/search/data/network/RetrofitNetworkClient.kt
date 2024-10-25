package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
        private val baseUrl = "https://itunes.apple.com"

        private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ITunesApiCreate = retrofit.create(ITunesSearchAPI::class.java)
    override fun doRequest(request: TracksSearchRequest): Response {
        return try {
            val response = ITunesApiCreate.search(request.entity,request.text).execute()
            val networkResponse = response.body()?: ITunesResponse(0, emptyList())//Response()
            Log.d("RESPONSE", "Response body: $networkResponse")
            networkResponse.apply {
                resultCode = response.code()
                Log.d("SUCCESS", "Успех ${response.code()}")
            }
        } catch (ex: Exception){
            Response().apply {
                resultCode = null
                //resultCode = 400
                Log.e("FAIL", "Ошибка $resultCode")
            }
        }
    }
}