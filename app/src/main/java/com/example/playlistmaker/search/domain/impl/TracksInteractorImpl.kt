package com.example.playlistmaker.search.domain.impl

import android.content.Context
import android.util.Log
import com.example.playlistmaker.search.data.network.SearchResultNetwork
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val context: Context
) :
    TracksInteractor {

    private val executor = Executors.newCachedThreadPool()
    private val checkInternetConnection = CheckInternetConnection()
    override fun searchTracks(
        request: TracksSearchRequest,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            try {
//                if (!checkInternetConnection.isOnline(context)) {
//                    throw Exception("Network is not available")
//                }

                when (val result = repository.searchTracks(request)) {
                    is SearchResultNetwork.Success -> consumer.consume(result.tracks)
                    is SearchResultNetwork.Empty -> consumer.onError(TracksInteractor.ErrorType.EMPTY_RESPONSE)
                    is SearchResultNetwork.Error -> consumer.onError(TracksInteractor.ErrorType.FAILURE)
                }
            } catch (e: Exception) {
                Log.e("TracksInteractor", "Error searching tracks", e)
                consumer.onError(TracksInteractor.ErrorType.FAILURE) // Ошибка запроса
            }
//            try {
//
//                if (!checkInternetConnection.isOnline(context)){
//                    throw Exception("Network is not available")
//                }
//                val tracksParceling = repository.searchTracks(request)
//                if (tracksParceling.isEmpty()) {
//                    consumer.onError(TracksInteractor.ErrorType.EMPTY_RESPONSE)
//                } else {
//                    consumer.consume(tracksParceling)
//                }
//            } catch (e: Exception) {
//                Log.e("TracksInteractor", "Error searching tracks", e)
//                consumer.onError(TracksInteractor.ErrorType.FAILURE)
//            }
        }
    }
}