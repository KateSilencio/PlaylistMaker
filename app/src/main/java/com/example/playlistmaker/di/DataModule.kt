package com.example.playlistmaker.di

import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.medialib.data.db.AppDatabase
import com.example.playlistmaker.medialib.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.medialib.data.impl.FileRepositoryImpl
import com.example.playlistmaker.medialib.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.medialib.domain.api.PlaylistRepository
import com.example.playlistmaker.medialib.domain.filestore.FileRepository
import com.example.playlistmaker.medialib.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.network.ITunesSearchAPI
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.DataStoreFunRepositoryImpl
import com.example.playlistmaker.search.data.sharedprefs.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.datastore.DataStoreFunRepository
import com.example.playlistmaker.search.domain.datastore.SearchHistoryLogicRepository
import com.example.playlistmaker.settings.data.ExternalNavigationImpl
import com.example.playlistmaker.settings.data.sharedpref.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.ExternalNavigation
import com.example.playlistmaker.settings.domain.datastore.SettingsRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

// ### Экран поиска ###
    single<ITunesSearchAPI>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchAPI::class.java)
    }
    single<NetworkClient> { RetrofitNetworkClient(ITunesApiCreate = get()) }
    single<TracksRepository> { TrackRepositoryImpl(networkClient = get(), favoriteTracksRepository = get()) }
    //для передачи контекста в sharedPref
    single<DataStoreFunRepository> { DataStoreFunRepositoryImpl(context = get()) }
    //для передачи в SearchHistoryRepositoryImpl
    single { Gson() }
    single<SearchHistoryLogicRepository> { SearchHistoryRepositoryImpl(
        gson = get(),
        sharedPref = get(),
        favoriteTracksRepository = get())
    }

// ### Экран настройки ###
    //контекст уже есть
    single<SettingsRepository> { SettingsRepositoryImpl(context = get()) }
    single<ExternalNavigation> { ExternalNavigationImpl(context = get()) }

// ### Экран медиаплеер ###
    factory { MediaPlayer() }
    factory<MediaPlayerRepository> { MediaPlayerRepositoryImpl(mediaPlayer = get())  }

// ### БД ###
    single {
        Room.databaseBuilder(context = androidContext(), AppDatabase::class.java,"database.db")
            .build()
    }

// ### MediaLibrary ###
    single<FavoriteTracksRepository> { FavoriteTracksRepositoryImpl(get()) }
    single<FileRepository>{FileRepositoryImpl(context = androidContext())}
    single<PlaylistRepository> { PlaylistRepositoryImpl(get())  }
}