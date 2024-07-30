package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.data.TracksFields
import com.google.gson.reflect.TypeToken
import java.util.LinkedList
import com.google.gson.Gson

class SearchHistory(private val sharedPref: SharedPreferences) {

    private val gson = Gson()
    fun mainLogicSaveTracks(tracks: TracksFields): LinkedList<TracksFields> {
        //1.Получаем список из SP
        val linkedListTracks = getTracks()

        //2.Список пустой - добавляем трек
        if (linkedListTracks.isEmpty()) {
            linkedListTracks.addFirst(tracks)
            saveTracks(linkedListTracks)
            return getTracks()
        }

        //3.Существует ли трек в списке
        val index = linkedListTracks.indexOf(tracks)
        if (index != -1) {

        //4.Если трек не на первом месте - удаляем, иначе - ничего не делаем
            if (index != 0) {
                linkedListTracks.removeAt(index)
            } else return getTracks()
        }

        Log.d("TRACKCLICK", "трек в начало ${tracks}")
        //5.Добавляем трек в начало
        linkedListTracks.addFirst(tracks)

        //6.Количество треков = 10, удаляем последний
        if (linkedListTracks.size > TRACK_COUNT) {
            linkedListTracks.removeLast()
        }

        //7.Сохраняем измененный список в SP
        saveTracks(linkedListTracks)
        return getTracks()
    }

    private fun saveTracks(tracks: LinkedList<TracksFields>) {
        val json = Gson().toJson(tracks)
        sharedPref.edit()
            .putString(EDIT_HISTORY_KEY, json)
            .apply()
    }

    fun getTracks(): LinkedList<TracksFields> {
        val json = sharedPref.getString(EDIT_HISTORY_KEY, null) ?: return LinkedList<TracksFields>()
        return json.let {
            val typeList = object : TypeToken<LinkedList<TracksFields>>() {}.type
            gson.fromJson(it, typeList)
        }
    }

    fun clearHistory() {
        sharedPref.edit()
            .remove(EDIT_HISTORY_KEY)
            .apply()
    }

    companion object {
        const val TRACK_COUNT = 10
    }
}