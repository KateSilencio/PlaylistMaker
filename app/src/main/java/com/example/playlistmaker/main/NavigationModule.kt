package com.example.playlistmaker.main

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class NavigationModule(private val context: Context) {

    fun goToSearchScreen(){
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    fun goToMediaScreen(){
        val mediaIntent = Intent(context, MediaActivity::class.java)
        context.startActivity(mediaIntent)
    }

    fun goToSettingsScreen(){
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }
}