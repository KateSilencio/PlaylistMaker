package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

class RetrofitNetworkClient(private val ITunesApiCreate: ITunesSearchAPI) : NetworkClient {

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