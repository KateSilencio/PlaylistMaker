package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.SharedPrefFunRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.sharedprefs.SearchHistoryLogicRepository
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.domain.usecase.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.usecase.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.search.domain.usecases.SearchHistoryUseCaseImpl
import com.example.playlistmaker.settings.data.ExternalNavigationImpl
import com.example.playlistmaker.settings.data.sharedpref.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.ExternalNavigation
import com.example.playlistmaker.settings.domain.sharedpref.SettingsRepository
import com.example.playlistmaker.settings.domain.sharedpref.usecase.SettingsInteractor
import com.example.playlistmaker.settings.domain.sharedpref.usecase.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.usecase.ExternalNavigationInteractor
import com.example.playlistmaker.settings.domain.usecase.ExternalNavigationInteractorImpl

object Creator {

    private lateinit var appContext: Context

    fun initialize(context: Context){
        this.appContext = context.applicationContext
    }

    fun provideSharedPrefFunRepository(): SharedPrefFunRepositoryImpl {
        return SharedPrefFunRepositoryImpl(appContext)
    }

    fun provideSearchHistoryRepository(): SearchHistoryLogicRepository {
        val sharedPrefFunRepository = provideSharedPrefFunRepository()
        return SearchHistoryRepositoryImpl(sharedPrefFunRepository)
    }

    fun provideSearchHistoryUseCase(): SearchHistoryUseCase {
        val historyRepository: SearchHistoryLogicRepository = provideSearchHistoryRepository()
        return SearchHistoryUseCaseImpl(historyRepository)
    }

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(), appContext)
    }

    //____________________________________________________
    fun provideSettingsInteractor(context: Context): SettingsInteractor{
        val settingsRepository: SettingsRepository = SettingsRepositoryImpl(context)
        return SettingsInteractorImpl(settingsRepository)
    }

    fun provideExternalNavigationInteractor(context: Context):ExternalNavigationInteractor{
        val externalNavigation: ExternalNavigation = ExternalNavigationImpl(context)
        return ExternalNavigationInteractorImpl(externalNavigation)
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        val mediaPlayerRepository = provideMediaPlayerRepository()
        return MediaPlayerInteractorImpl(mediaPlayerRepository)
    }
}