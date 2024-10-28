package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.settings.ui.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SWITCH_PREFERENCES = "practicum_example_preferences"
const val SWITCH_IS_CHECKED = "switch_is_checked"

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Активация тулбара для окна Настройки
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Подписка на изменения во ViewModel
        //смотрим переключатель темы
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_theme)
        settingsViewModel.isCheckedThemeLive.observe(
            this,
            Observer { isChecked ->
                themeSwitcher.isChecked = isChecked
                //(applicationContext as App).switchTheme(isChecked)
            }
        )

        //интент для отправки ссылки с выбором приложения
        val layoutShare = findViewById<LinearLayout>(R.id.LayoutShare)
        layoutShare.setOnClickListener{
            settingsViewModel.shareLink(applicationContext.getString(R.string.android_developer_link))
        }

        //интент для отправки сообщения по электронной почте
        val layoutSupport = findViewById<LinearLayout>(R.id.LayoutSupport)
        layoutSupport.setOnClickListener{
            settingsViewModel.emailSupport(
                applicationContext.getString(R.string.student_mail),
                applicationContext.getString(R.string.topic_mail),
                applicationContext.getString(R.string.message_mail)
            )
        }

        //интент для запуска браузера с веб страницей
        val layoutAgreement = findViewById<LinearLayout>(R.id.LayoutAgreement)
        layoutAgreement.setOnClickListener{
            settingsViewModel.agreementLink(applicationContext.getString(R.string.agreement_link))
        }

        //Cохранение переключателя темы
        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            settingsViewModel.saveTheme(SWITCH_PREFERENCES, SWITCH_IS_CHECKED,isChecked)
        }
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}