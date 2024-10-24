package com.example.playlistmaker.settings.domain.datastore

interface SettingsRepository {

    fun getTheme(name: String,key: String,value: Boolean): Boolean

    fun saveTheme(name: String, key: String, isChecked: Boolean)

}