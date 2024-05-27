package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search_btn_id = findViewById<com.google.android.material.button.MaterialButton>(R.id.search_btn)
        val media_btn_id = findViewById<com.google.android.material.button.MaterialButton>(R.id.media_btn)
        val settings_btn_id = findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_btn)

        //1. Анонимный класс
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Toast.makeText(this@MainActivity, "Нажали на Поиск!", Toast.LENGTH_SHORT).show()
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        search_btn_id.setOnClickListener(searchClickListener)

        //2. Лямбда выражение
        media_btn_id.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Нажали на Медиатека!", Toast.LENGTH_SHORT).show()
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settings_btn_id.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Нажали на Настройки!", Toast.LENGTH_SHORT).show()
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}