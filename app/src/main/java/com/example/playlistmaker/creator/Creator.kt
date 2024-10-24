package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.DataStoreFunRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.datastore.SearchHistoryLogicRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.data.ExternalNavigationImpl
import com.example.playlistmaker.settings.data.sharedpref.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.ExternalNavigation
import com.example.playlistmaker.settings.domain.datastore.SettingsRepository
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ExternalNavigationInteractor
import com.example.playlistmaker.settings.domain.interactor.ExternalNavigationInteractorImpl

object Creator {

    private lateinit var appContext: Context

    fun initialize(context: Context){
        this.appContext = context.applicationContext
    }

    fun provideSharedPrefFunRepository(): DataStoreFunRepositoryImpl {
        return DataStoreFunRepositoryImpl(appContext)
    }

    fun provideSearchHistoryRepository(): SearchHistoryLogicRepository {
        val sharedPrefFunRepository = provideSharedPrefFunRepository()
        return SearchHistoryRepositoryImpl(sharedPrefFunRepository)
    }

    fun provideSearchHistoryUseCase(): SearchHistoryInteractor {
        val historyRepository: SearchHistoryLogicRepository = provideSearchHistoryRepository()
        return SearchHistoryInteractorImpl(historyRepository)
    }

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
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