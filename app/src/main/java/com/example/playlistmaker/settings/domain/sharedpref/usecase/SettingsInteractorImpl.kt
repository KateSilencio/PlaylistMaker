package com.example.playlistmaker.settings.domain.sharedpref.usecase

import com.example.playlistmaker.settings.domain.sharedpref.SettingsRepository

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