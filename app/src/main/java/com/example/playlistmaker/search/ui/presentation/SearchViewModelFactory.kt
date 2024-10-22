package com.example.playlistmaker.search.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.usecases.SearchHistoryUseCase

class SearchViewModelFactory(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryUseCase: SearchHistoryUseCase
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(tracksInteractor,searchHistoryUseCase) as T
    }
}