package com.example.playlistmaker.medialib.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.medialib.data.model.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun tracksDAO(): TracksDAO
}