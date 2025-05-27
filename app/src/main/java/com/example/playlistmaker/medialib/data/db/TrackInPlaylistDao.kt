package com.example.playlistmaker.medialib.data.db

import androidx.room.Dao
import androidx.room.Delete
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

    //***********************************************************
    @Query("SELECT * FROM track_in_playlist_table WHERE trackID IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<Long>): List<TrackInPlaylistEntity>

    //для списка треков в плейлисте
    //удаление трека как entity нужно ли???
    @Delete
    suspend fun deleteTrack(track: TrackInPlaylistEntity)

    //удаление трека по id
    @Query("DELETE FROM track_in_playlist_table WHERE trackID = :trackId")
    suspend fun deleteTrack(trackId: Int)

}