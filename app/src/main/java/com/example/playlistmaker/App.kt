package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractor
import com.example.playlistmaker.settings.ui.SWITCH_IS_CHECKED
import com.example.playlistmaker.settings.ui.SWITCH_PREFERENCES

class App : Application() {

    var darkTheme = false
    private val settingsInteractor: SettingsInteractor by lazy {
        Creator.provideSettingsInteractor(context = this)
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initialize(this)

        //Восстанавливаем значение Switch
        darkTheme = settingsInteractor.onGetTheme(
            SWITCH_PREFERENCES,
            SWITCH_IS_CHECKED,
            false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
