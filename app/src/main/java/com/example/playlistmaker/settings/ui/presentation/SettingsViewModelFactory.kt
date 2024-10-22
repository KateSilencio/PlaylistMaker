package com.example.playlistmaker.settings.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.sharedpref.usecase.SettingsInteractor
import com.example.playlistmaker.settings.domain.usecase.ExternalNavigationInteractor

class SettingsViewModelFactory(
    private val settingsInteractor: SettingsInteractor,
    private val externalNavigationInteractor: ExternalNavigationInteractor
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsInteractor,externalNavigationInteractor) as T
    }
}