package com.example.playlistmaker.domain.impl

import android.content.Context
import android.util.Log
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.TracksSearchRequest
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val context: Context) :
    TracksInteractor {

    private val executor = Executors.newCachedThreadPool()
    private val checkInternetConnection = CheckInternetConnection()
    override fun searchTracks(
        request: TracksSearchRequest,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            try {

                if (!checkInternetConnection.isOnline(context)){
                    throw Exception("Network is not available")
                }
                val tracksParceling = repository.searchTracks(request)
                if (tracksParceling.isEmpty()) {
                    consumer.onError(TracksInteractor.ErrorType.EMPTY_RESPONSE)
                } else {
                    consumer.consume(tracksParceling)
                }
            } catch (e: Exception) {
                Log.e("TracksInteractor", "Error searching tracks", e)
                consumer.onError(TracksInteractor.ErrorType.FAILURE)
            }
        }
    }
}