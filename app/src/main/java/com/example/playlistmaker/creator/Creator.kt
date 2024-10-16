package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.media.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.sharedprefs.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.sharedprefs.SharedPrefFunRepositoryImpl
import com.example.playlistmaker.data.sharedprefs.usecases.SearchHistoryUseCase
import com.example.playlistmaker.data.sharedprefs.usecases.SearchHistoryUseCaseImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.media.MediaPlayerRepository
import com.example.playlistmaker.domain.api.media.usecase.MediaPlayerUseCase
import com.example.playlistmaker.domain.api.media.usecase.MediaPlayerUseCaseImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.sharedprefs.SearchHistoryLogicRepository

object Creator {

    private lateinit var context: Context

    fun initialize(context: Context){
        this.context = context.applicationContext
    }

    fun provideSharedPrefFunRepository(): SharedPrefFunRepositoryImpl {
        return SharedPrefFunRepositoryImpl(context)
    }

    fun provideSearchHistoryRepository(): SearchHistoryLogicRepository {
        val sharedPrefFunRepository = provideSharedPrefFunRepository()
        return SearchHistoryRepositoryImpl(sharedPrefFunRepository)
    }

    fun provideSearchHistoryUseCase(): SearchHistoryUseCase {
        val historyRepository: SearchHistoryLogicRepository = provideSearchHistoryRepository()
        return SearchHistoryUseCaseImpl(historyRepository)
    }

    private fun getTracksRepository(): TracksRepository{
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository(), context)
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerUseCase(): MediaPlayerUseCase {
        val mediaPlayerRepository = provideMediaPlayerRepository()
        return MediaPlayerUseCaseImpl(mediaPlayerRepository)
    }
}