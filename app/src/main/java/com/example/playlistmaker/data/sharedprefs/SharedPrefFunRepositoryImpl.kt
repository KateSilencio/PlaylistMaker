package com.example.playlistmaker.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.sharedprefs.SharedPrefFunRepository

class SharedPrefFunRepositoryImpl(private val context: Context): SharedPrefFunRepository{

    private fun getSharedPreferences(key: String):SharedPreferences{
        return context.getSharedPreferences(key,Context.MODE_PRIVATE)
    }

    override fun saveString(name:String,key: String, value: String){
        getSharedPreferences(name).edit().putString(key, value).apply()
    }

    override fun getString(name:String,key: String): String? {
        return getSharedPreferences(name).getString(key, null)
    }

    override fun saveBoolean(name:String,key: String, value: Boolean){
        getSharedPreferences(name).edit().putBoolean(key,value).apply()
    }

    override fun getBoolean(name: String,key: String,value: Boolean): Boolean {
        return getSharedPreferences(name).getBoolean(key,value)
    }

    override fun remove(name:String,key: String) {
        getSharedPreferences(name).edit().remove(key).apply()
    }

}