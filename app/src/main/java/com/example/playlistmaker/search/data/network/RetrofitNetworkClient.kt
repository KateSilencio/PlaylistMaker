package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val ITunesApiCreate: ITunesSearchAPI) : NetworkClient {

    override suspend fun doRequest(request: TracksSearchRequest): Response {
//        return try {
//            val response = ITunesApiCreate.search(request.entity,request.text).execute()
//            val networkResponse = response.body()?: ITunesResponse(0, emptyList())//Response()
//            Log.d("RESPONSE", "Response body: $networkResponse")
//            networkResponse.apply {
//                resultCode = response.code()
//                Log.d("SUCCESS", "Успех ${response.code()}")
//            }
//        } catch (ex: Exception){
//            Response().apply {
//                resultCode = null
//                //resultCode = 400
//                Log.e("FAIL", "Ошибка $resultCode")
//            }
//        }

        return withContext(Dispatchers.IO){
            try {
                val networkResponse = ITunesApiCreate.search(request.entity,request.text)
                networkResponse.apply {
                    resultCode = 200
                    Log.d("SUCCESS", "Успех код 200")
                }
            } catch (ex: Exception){
                Response().apply {
                    Log.e("FAIL", "Ошибка: ${ex.message}")
                    resultCode = null
                }
            }
        }
    }
}