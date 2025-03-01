package com.example.playlistmaker.di

import com.example.playlistmaker.medialib.ui.presentation.FavoriteTracksViewModel
import com.example.playlistmaker.medialib.ui.presentation.PlaylistsViewModel
import com.example.playlistmaker.player.ui.presenter.MediaViewModel
import com.example.playlistmaker.search.ui.presentation.SearchViewModel
import com.example.playlistmaker.settings.ui.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
// ### Экран поиска ###
    viewModel {
        SearchViewModel(tracksInteractor = get(), searchHistoryInteractor = get())
    }
// ### Экран настройки ###
    viewModel {
        SettingsViewModel(settingsInteractor = get(), externalNavigationInteractor = get())
    }
// ### Экран медиаплеер ###
    viewModel {
        MediaViewModel(mediaPlayerInteractor = get(), favoriteTracksInteractor = get())
    }
// ### Фрагмент Избранные треки ###
    viewModel {
        FavoriteTracksViewModel(favoriteTracksInteractor = get())
    }

// ### Фрагмент Плейлисты ###
    viewModel {
        PlaylistsViewModel()
    }
}