package com.example.playlistmaker.search.domain.impl

import android.util.Log
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) :
    TracksInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(
        request: TracksSearchRequest,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            try {
                when (val result = repository.searchTracks(request)) {
                    is SearchResultNetwork.Success -> consumer.consume(result.tracks)
                    is SearchResultNetwork.Empty -> consumer.onError(TracksInteractor.ErrorType.EMPTY_RESPONSE)
                    is SearchResultNetwork.Error -> consumer.onError(TracksInteractor.ErrorType.FAILURE)
                }
            } catch (e: Exception) {
                Log.e("TracksInteractor", "Error searching tracks", e)
                consumer.onError(TracksInteractor.ErrorType.FAILURE)
            }
        }
    }
}