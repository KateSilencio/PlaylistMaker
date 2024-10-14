package com.example.playlistmaker.data.sharedprefs

import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.domain.sharedprefs.SearchHistoryLogicRepository
import com.example.playlistmaker.domain.sharedprefs.SharedPrefFunRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.LinkedList

class SearchHistoryRepositoryImpl(private val sharedPref: SharedPrefFunRepository) :
    SearchHistoryLogicRepository {

    private val gson = Gson()
    override fun saveTrack(track: TracksParceling): LinkedList<TracksParceling> {

        fun saveTracks(tracks: LinkedList<TracksParceling>) {
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

    override fun getTracks(): LinkedList<TracksParceling> {
        val json = sharedPref.getString(SEARCH_HISTORY, EDIT_HISTORY_KEY)
            ?: return LinkedList<TracksParceling>()
        return json.let {
            val typeList = object : TypeToken<LinkedList<TracksParceling>>() {}.type
            gson.fromJson(it, typeList)
        }
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