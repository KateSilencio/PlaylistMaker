package com.example.playlistmaker.search.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import com.example.playlistmaker.search.ui.presentation.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private var lastRequestStr = ""
    }

    private val searchState = MutableLiveData<SearchState>()
    val searchStateLive: LiveData<SearchState> = searchState

    private var searchJob: Job? = null
    private var clickJob: Job? = null

    init {
        //Дефолтное состояние при включении
        searchState.value = SearchState.Default
    }

    fun executeRequest(inputText: String) {
        lastRequestStr = inputText
        searchState.value = SearchState.Loading

        val request = TracksSearchRequest(entity = "song", text = inputText)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            tracksInteractor.searchTracks(request).collect{ result ->
                when(result){
                    is SearchResultNetwork.Success -> {
                        val foundTracks = result.tracks
                        if (foundTracks.isEmpty()) {
                            searchState.postValue(SearchState.NothingFound)
                        } else {
                            searchState.postValue(SearchState.TrackSearchResults(foundTracks))
                        }
                    }
                    is SearchResultNetwork.Empty -> {
                        searchState.postValue(SearchState.NothingFound)
                    }
                    is SearchResultNetwork.Error ->{
                        searchState.postValue(SearchState.ConnectionError(400))
                    }
                }
            }
        }
    }

    //Используется при нажатии Очистить историю
    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        searchState.value = SearchState.NoData
    }

    fun onDebounce(inputText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (inputText.isNotEmpty()) {
                executeRequest(inputText)
            }
        }

    }

    fun onTrackClick(track: TracksData, trackSelected: (TracksData) -> Unit){
        clickJob?.cancel()
        clickJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            trackSelected(track)
        }

    }

    fun onSaveTrackInHistory(track: TracksData) {
        viewModelScope.launch {
            searchHistoryInteractor.saveTrack(track)
            onShowHistory()
        }
    }

    fun onShowHistory() {
        viewModelScope.launch {
            val history = searchHistoryInteractor.getTracks()
            if (history.isNotEmpty()) {
                searchState.value = SearchState.TrackSearchHistory(history)
            }
        }
    }

    fun updateRequest(){
        executeRequest(lastRequestStr)
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}


