package com.example.playlistmaker.data.sharedprefs.usecases

import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.domain.sharedprefs.SearchHistoryLogicRepository
import java.util.LinkedList

class SearchHistoryUseCaseImpl(private val historyRepository: SearchHistoryLogicRepository) : SearchHistoryUseCase {
    override fun saveTrack(track: TracksParceling): LinkedList<TracksParceling> {
        return historyRepository.saveTrack(track)
    }

    override fun getTracks(): LinkedList<TracksParceling> {
        return historyRepository.getTracks()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}