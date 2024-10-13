package com.example.playlistmaker.ui.settings

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.sharedprefs.SharedPrefFunRepository

class App : Application() {

    var darkTheme = false
    private val sharedPrefFunRepository: SharedPrefFunRepository by lazy {
        Creator.provideSharedPrefFunRepository()
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initialize(this)

        //Восстанавливаем значение Switch
        darkTheme = sharedPrefFunRepository.getBoolean(
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
