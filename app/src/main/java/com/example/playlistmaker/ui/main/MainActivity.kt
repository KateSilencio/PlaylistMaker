package com.example.playlistmaker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {

    private lateinit var navigation: NavigationModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.search_btn)
        val mediaBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.media_btn)
        val settingsBtn =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.settings_btn)

        navigation = NavigationModule(this)
        searchBtn.setOnClickListener{
            navigation.goToSearchScreen()
        }

        mediaBtn.setOnClickListener {
            navigation.goToMediaScreen()
        }

        settingsBtn.setOnClickListener {
            navigation.goToSettingsScreen()
        }
    }
}