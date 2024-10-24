package com.example.playlistmaker.settings.domain.datastore.interactor

import com.example.playlistmaker.settings.domain.datastore.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {
    override fun onSaveTheme(name: String, key: String, value: Boolean) {
        settingsRepository.saveTheme(name, key, value)
    }

    override fun onGetTheme(name: String, key: String, value: Boolean): Boolean {
        return settingsRepository.getTheme(name, key, value)
    }
}