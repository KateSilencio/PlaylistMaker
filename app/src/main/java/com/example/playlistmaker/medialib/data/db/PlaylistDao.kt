package com.example.playlistmaker.medialib.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?

    @Query("SELECT * FROM playlists_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    //****************************************************
    //постоянное наблюдение за изменениями плейлиста
    @Query("SELECT * FROM playlists_table WHERE playlistId = :id")
    fun getPlaylistByIdFlow(id: Long): Flow<PlaylistEntity>

    //для списка треков в плейлисте
    //для синхронной проверки всех плейлистов
    @Query("SELECT * FROM playlists_table")
    suspend fun getAllPlaylistsSync(): List<PlaylistEntity>

}