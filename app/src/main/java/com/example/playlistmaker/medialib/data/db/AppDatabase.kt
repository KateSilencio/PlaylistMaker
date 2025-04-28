package com.example.playlistmaker.medialib.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.medialib.data.model.TrackEntity
import com.example.playlistmaker.medialib.data.model.TrackInPlaylistEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TrackInPlaylistEntity::class
    ])
abstract class AppDatabase: RoomDatabase() {

    abstract fun tracksDAO(): TracksDAO
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}