package com.example.playlistmaker.search.ui.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.TracksSearchRequest
import com.example.playlistmaker.player.domain.models.TracksParceling
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
       // searchState.value = SearchState(historyTracks = searchHistoryUseCase.getTracks())

        val initialHistoryTracks = searchHistoryUseCase.getTracks()
        searchState.value = SearchState(historyTracks = initialHistoryTracks)
        Log.d("AAA", "History tracks in initialHistoryTracks : $initialHistoryTracks")
    }

    //Используется при нажатии на основной список поиска
    fun onSaveTrackInHistory(track: TracksParceling) {
        val updatedHistory = searchHistoryUseCase.saveTrack(track)
        searchState.value = searchState.value?.copy(historyTracks = updatedHistory)

        Log.d("AAA", "History tracks in onSaveTrackInHistory : $updatedHistory")
    }

    //Используется при нажатии Очистить историю
    fun clearHistory() {
        searchHistoryUseCase.clearHistory()
        val updatedTracks = searchHistoryUseCase.getTracks()
        searchState.value = searchState.value?.copy(historyTracks = updatedTracks)
    }

    //Используется при работе с фокусом
    fun getTracksFromHistory() {
        val trackListHistory = searchHistoryUseCase.getTracks()
        searchState.value = searchState.value?.copy(historyTracks = trackListHistory)
    }

    fun onDebounce(inputText: String){
        // удалим предыдущий runnable, если он существует
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
        runnable = Runnable {
            if (inputText.isNotEmpty()) {
                executeRequest(inputText)
            } else {
                //для очистки экрана
                searchState.value = searchState.value?.copy(isClearScreenFlag = true)
            }
        }
        runnable.let {
            handler.postDelayed(it, SEARCH_DEBOUNCE_DELAY)
        }
    }

    fun executeRequest(inputText: String) {
        lastRequestStr = inputText
        //ProgressBar
        searchState.value = searchState.value?.copy(isProgressBarVisible = true)
        //для очистки экрана
        searchState.value = searchState.value?.copy(isClearScreenFlag = true)

        val request = TracksSearchRequest(entity = "song", text = inputText)
        tracksInteractor.searchTracks(request,object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<TracksParceling>) {
                //Успех
                searchState.postValue(searchState.value?.copy(      //handler.post
                    tracks = foundTracks,
                    isProgressBarVisible = false,
                    errorType = TracksInteractor.ErrorType.SUCCESS))
            }

            override fun onError(error: TracksInteractor.ErrorType) {
                searchState.postValue(searchState.value?.copy(
                    isProgressBarVisible = false,
                    tracks = emptyList(),
                    errorType = error
                ))
            }

        })
    }

    fun updateRequest() {
        executeRequest(lastRequestStr)
    }
    override fun onCleared() {
        super.onCleared()
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }


}