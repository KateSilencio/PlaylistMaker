package com.example.playlistmaker.search.data.sharedprefs

import com.example.playlistmaker.medialib.domain.FavoriteTracksRepository
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.search.domain.datastore.DataStoreFunRepository
import com.example.playlistmaker.search.domain.datastore.SearchHistoryLogicRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.util.LinkedList

class SearchHistoryRepositoryImpl(
    private val sharedPref: DataStoreFunRepository,
    private val gson: Gson,
    private val favoriteTracksRepository: FavoriteTracksRepository
) : SearchHistoryLogicRepository {

    //private val gson = Gson()
    override suspend fun saveTrack(track: TracksData): LinkedList<TracksData> {

        fun saveTracks(tracks: LinkedList<TracksData>) {
            val json = Gson().toJson(tracks)
            sharedPref.saveString(SEARCH_HISTORY, EDIT_HISTORY_KEY, json)
        }

        //1.Получаем список из SP
        val linkedListTracks = getTracks()

        //2.Список пустой - добавляем трек
        if (linkedListTracks.isEmpty()) {
            linkedListTracks.addFirst(track)
            saveTracks(linkedListTracks)
            return getTracks()
        }

        //3.Существует ли трек в списке
        val index = linkedListTracks.indexOf(track)
        if (index != -1) {

            //4.Если трек не на первом месте - удаляем, иначе - ничего не делаем
            if (index != 0) {
                linkedListTracks.removeAt(index)
            } else return getTracks()
        }

        //5.Добавляем трек в начало
        linkedListTracks.addFirst(track)

        //6.Количество треков = 10, удаляем последний
        if (linkedListTracks.size > TRACK_COUNT) {
            linkedListTracks.removeLast()
        }

        //7.Сохраняем измененный список в SP
        saveTracks(linkedListTracks)
        return getTracks()

    }

    override suspend fun getTracks(): LinkedList<TracksData> {
        val json = sharedPref.getString(SEARCH_HISTORY, EDIT_HISTORY_KEY)
            ?: return LinkedList<TracksData>()

        val tracks = gson.fromJson<LinkedList<TracksData>>(json,object : TypeToken<LinkedList<TracksData>>() {}.type)
        val favoriteTracks = favoriteTracksRepository
            .getAllFavoriteTracks()
            .first()
            .map { it.trackID }
        tracks.forEach { track ->
            track.isFavorite = favoriteTracks.contains(track.trackID)
        }
        return tracks
//        return json.let {
//            val typeList = object : TypeToken<LinkedList<TracksData>>() {}.type
//            gson.fromJson(it, typeList)
//        }
    }

    override fun clearHistory() {
        sharedPref.remove(SEARCH_HISTORY, EDIT_HISTORY_KEY)
    }

    companion object {
        const val TRACK_COUNT = 10
        const val EDIT_HISTORY_KEY = "edit_history_key"
        const val SEARCH_HISTORY = "search_history"
    }
}