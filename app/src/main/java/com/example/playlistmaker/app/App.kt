package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.datastore.interactor.SettingsInteractor
import com.example.playlistmaker.settings.ui.SWITCH_IS_CHECKED
import com.example.playlistmaker.settings.ui.SWITCH_PREFERENCES
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    var darkTheme = false

    //извлечения напрямую из Koin
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        //Dependency Injection
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(dataModule,domainModule,viewModelModule))
        }

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
