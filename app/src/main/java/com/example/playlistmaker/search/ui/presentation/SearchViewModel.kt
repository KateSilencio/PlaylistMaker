package com.example.playlistmaker.search.ui.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.models.TracksParceling
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import com.example.playlistmaker.search.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.search.ui.presentation.models.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryUseCase: SearchHistoryUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private var lastRequestStr = ""
    }

    private val searchState = MutableLiveData<SearchState>()
    val searchStateLive: LiveData<SearchState> = searchState

    //потоки
    private lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())

    init {
        //Дефолтное состояние при включении
        searchState.value = SearchState.Default
    }

    fun executeRequest(inputText: String) {
        lastRequestStr = inputText

        searchState.value = SearchState.Loading

        val request = TracksSearchRequest(entity = "song", text = inputText)
        tracksInteractor.searchTracks(request, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<TracksParceling>) {
                if (foundTracks.isEmpty()) {
                    searchState.postValue(SearchState.NothingFound)
                } else {
                    searchState.postValue(SearchState.TrackSearchResults(foundTracks))
                }
            }

            override fun onError(error: TracksInteractor.ErrorType) {
                searchState.postValue(SearchState.ConnectionError(400))
            }
        })
    }

    //Используется при нажатии Очистить историю
    fun clearHistory() {
        searchHistoryUseCase.clearHistory()
        searchState.value = SearchState.NoData
    }

    fun onDebounce(inputText: String) {
        // удалим предыдущий runnable, если он существует
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
        runnable = Runnable {
            if (inputText.isNotEmpty()) {
                executeRequest(inputText)
            }
        }
        handler.postDelayed(runnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onSaveTrackInHistory(track: TracksParceling) {
        searchHistoryUseCase.saveTrack(track)
        onShowHistory()
    }

    fun onShowHistory() {
        val history = searchHistoryUseCase.getTracks()
        if (history.isNotEmpty()){
            searchState.value = SearchState.TrackSearchHistory(history)
        }

    }

    fun updateRequest(){
        executeRequest(lastRequestStr)
    }

    override fun onCleared() {
        super.onCleared()
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }
}


