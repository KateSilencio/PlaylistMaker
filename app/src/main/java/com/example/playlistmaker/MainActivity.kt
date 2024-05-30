package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.search_btn)
        val mediaBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.media_btn)
        val settingsBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_btn)

        //1. Анонимный класс
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Toast.makeText(this@MainActivity, "Нажали на Поиск!", Toast.LENGTH_SHORT).show()
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        searchBtn.setOnClickListener(searchClickListener)

        //2. Лямбда выражение
        mediaBtn.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Нажали на Медиатека!", Toast.LENGTH_SHORT).show()
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsBtn.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Нажали на Настройки!", Toast.LENGTH_SHORT).show()
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}