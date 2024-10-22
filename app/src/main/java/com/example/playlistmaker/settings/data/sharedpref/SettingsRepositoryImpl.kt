package com.example.playlistmaker.settings.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.sharedpref.SettingsRepository

class SettingsRepositoryImpl(private val context: Context): SettingsRepository {

    private fun getSharedPreferences(key: String): SharedPreferences {
        return context.getSharedPreferences(key,Context.MODE_PRIVATE)
    }

    override fun getTheme(name: String, key: String, value: Boolean): Boolean {
        return getSharedPreferences(name).getBoolean(key,value)
    }

    override fun saveTheme(name: String, key: String, isChecked: Boolean) {
        getSharedPreferences(name).edit().putBoolean(key,isChecked).apply()
    }
}