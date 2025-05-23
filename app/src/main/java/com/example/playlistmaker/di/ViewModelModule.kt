package com.example.playlistmaker.di

import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.ui.presentation.EditPlaylistViewModel
import com.example.playlistmaker.medialib.ui.presentation.FavoriteTracksViewModel
import com.example.playlistmaker.medialib.ui.presentation.PlaylistScreenViewModel
import com.example.playlistmaker.medialib.ui.presentation.PlaylistShowViewModel
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
        MediaViewModel(
            mediaPlayerInteractor = get(),
            favoriteTracksInteractor = get(),
            playlistInteractor = get()
        )
    }
// ### Фрагмент Избранные треки ###
    viewModel {
        FavoriteTracksViewModel(favoriteTracksInteractor = get())
    }

// ### Фрагмент Плейлисты ###
    viewModel {
        PlaylistsViewModel(playlistInteractor = get())
    }

    viewModel {
        PlaylistShowViewModel(playlistInteractor = get())
    }

    viewModel {
        PlaylistScreenViewModel(playlistInteractor = get(), playlistShareInteractor = get())
    }
// ### Фрагмент Редактировать плейлист ###
    viewModel { (playlist: Playlist) ->
        EditPlaylistViewModel(
            playlistInteractor = get(),
            playlist = playlist
        )
    }
}