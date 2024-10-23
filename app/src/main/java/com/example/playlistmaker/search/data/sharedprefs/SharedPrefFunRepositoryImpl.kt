package com.example.playlistmaker.search.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefFunRepository

class SharedPrefFunRepositoryImpl(private val context: Context): SharedPrefFunRepository {

    private fun getSharedPreferences(key: String):SharedPreferences{
        return context.getSharedPreferences(key,Context.MODE_PRIVATE)
    }

    override fun saveString(name:String,key: String, value: String){
        getSharedPreferences(name).edit().putString(key, value).apply()
    }

    override fun getString(name:String,key: String): String? {
        return getSharedPreferences(name).getString(key, null)
    }

    override fun remove(name:String,key: String) {
        getSharedPreferences(name).edit().remove(key).apply()
    }

}