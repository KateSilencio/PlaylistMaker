package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ExternalNavigationInteractor
import com.example.playlistmaker.settings.domain.interactor.ExternalNavigationInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val domainModule = module {

// ### для экрана поиска ###
    single<ExecutorService> { Executors.newCachedThreadPool() }

    factory <SearchHistoryInteractor> { SearchHistoryInteractorImpl(historyRepository = get()) }
    factory<TracksInteractor> { TracksInteractorImpl(repository = get(), executor = get()) }

// ### для экрана настройки ###
    factory<SettingsInteractor> { SettingsInteractorImpl(settingsRepository = get()) }
    factory<ExternalNavigationInteractor> { ExternalNavigationInteractorImpl(externalNavigation = get()) }

// ### для экрана плеер ###
    factory<MediaPlayerInteractor> { MediaPlayerInteractorImpl(mediaPlayerRepository = get()) }

}