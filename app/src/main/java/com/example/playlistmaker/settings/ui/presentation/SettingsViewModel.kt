package com.example.playlistmaker.settings.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.ExternalNavigationInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val externalNavigationInteractor: ExternalNavigationInteractor
) : ViewModel() {

    private val isCheckedTheme = MutableLiveData<Boolean>()
    val isCheckedThemeLive: LiveData<Boolean> = isCheckedTheme

    fun shareLink(link: String) {
        externalNavigationInteractor.onShareLink(link)
    }

    fun emailSupport(email: String, subject: String, text: String) {
        externalNavigationInteractor.onEmailSupport(email, subject, text)
    }

    fun agreementLink(url: String) {
        externalNavigationInteractor.onOpenAgreementLink(url)
    }

    fun saveTheme(name: String, key: String, isChecked: Boolean) {
        settingsInteractor.onSaveTheme(name,key,isChecked)
    }
}