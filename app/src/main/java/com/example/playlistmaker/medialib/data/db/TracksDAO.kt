package com.example.playlistmaker.medialib.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.medialib.data.model.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track : TrackEntity)

    @Query("SELECT * FROM tracks_table ORDER BY addedTime DESC")
    fun getAllTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM tracks_table")
    fun getAllTracksById(): Flow<List<Int>>
}