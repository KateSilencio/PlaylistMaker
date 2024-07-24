package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.android.material.switchmaterial.SwitchMaterial

const val SWITCH_PREFERENCES = "practicum_example_preferences"
const val SWITCH_IS_CHECKED = "switch_is_checked"

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Активация тулбара для окна Настройки
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //интент для отправки ссылки с выбором приложения
        val layoutShare = findViewById<LinearLayout>(R.id.LayoutShare)
        layoutShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT,getString(R.string.android_developer_link))
                type = "text/plain"
            }
            val shareChooserIntent = Intent.createChooser(shareIntent,null)
            if (shareChooserIntent.resolveActivity(packageManager)!=null){
                startActivity(shareChooserIntent)
            }
        }

        //интент для отправки сообщения по электронной почте
        val layoutSupport = findViewById<LinearLayout>(R.id.LayoutSupport)
        layoutSupport.setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_mail)))
                putExtra(Intent.EXTRA_SUBJECT,getString(R.string.topic_mail))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.message_mail))
                startActivity(this)
            }
        }

        //интент для запуска браузера с веб страницей
        val layoutAgreement = findViewById<LinearLayout>(R.id.LayoutAgreement)
        layoutAgreement.setOnClickListener{
            val site = Uri.parse(getString(R.string.agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, site)
            if (agreementIntent.resolveActivity(packageManager)!=null){
                startActivity(agreementIntent)
            }
        }

        val sharedPrefs = getSharedPreferences(SWITCH_PREFERENCES, MODE_PRIVATE)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_theme)
        //Переключатель темы + сохранение
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putBoolean(SWITCH_IS_CHECKED,checked)
                .apply()
        }
    }
    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}