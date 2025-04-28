package com.example.playlistmaker.medialib.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val title: String,
    val description: String?,
    val coverPath: String?,
    val tracksJson: String,
    val trackCount: Int
) {
    //строка Json в список Id
    fun getTrackIds(): List<Long> {
        return if (tracksJson.isNotEmpty()) {
            Gson().fromJson(tracksJson, object : TypeToken<List<Long>>() {}.type)
        } else {
            emptyList()
        }
    }

    //данные -> PlaylistEntity
    companion object {
        fun create(
            title: String,
            description: String?,
            coverPath: String?,
            trackIds: List<Long> = emptyList()
        ): PlaylistEntity {
            return PlaylistEntity(
                playlistId = 0, //автоинкремент?
                title = title,
                description = description,
                coverPath = coverPath,
                tracksJson = Gson().toJson(trackIds),
                trackCount = trackIds.size
            )
        }
    }
}