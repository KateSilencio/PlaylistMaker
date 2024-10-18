package com.example.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.sharedprefs.SharedPrefFunRepository
import com.google.android.material.switchmaterial.SwitchMaterial

const val SWITCH_PREFERENCES = "practicum_example_preferences"
const val SWITCH_IS_CHECKED = "switch_is_checked"

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsEventManager: SettingsEventManager

    private val sharedPrefFunRepository: SharedPrefFunRepository by lazy {
        Creator.provideSharedPrefFunRepository()
    }
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Creator.initialize(this)
        settingsEventManager = SettingsEventManager(this,sharedPrefFunRepository)

        //Активация тулбара для окна Настройки
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //интент для отправки ссылки с выбором приложения
        val layoutShare = findViewById<LinearLayout>(R.id.LayoutShare)
        layoutShare.setOnClickListener{
            settingsEventManager.shareLink()
        }

        //интент для отправки сообщения по электронной почте
        val layoutSupport = findViewById<LinearLayout>(R.id.LayoutSupport)
        layoutSupport.setOnClickListener{
            settingsEventManager.emailSupport()
        }

        //интент для запуска браузера с веб страницей
        val layoutAgreement = findViewById<LinearLayout>(R.id.LayoutAgreement)
        layoutAgreement.setOnClickListener{
            settingsEventManager.agreementLink()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_theme)
        //Переключатель темы + сохранение
        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            settingsEventManager.saveThemeSharedPref(isChecked)
        }
    }
    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}