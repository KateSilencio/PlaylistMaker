package com.example.playlistmaker.settings.domain.sharedpref.usecase

interface SettingsInteractor {

    fun onSaveTheme(name: String, key: String, value: Boolean)

    fun onGetTheme(name: String, key: String, value: Boolean): Boolean
}