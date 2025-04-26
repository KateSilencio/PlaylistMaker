package com.example.playlistmaker.medialib.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.medialib.data.model.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackID = :trackId")
    suspend fun getTrackById(trackId: Int): TrackInPlaylistEntity?

}