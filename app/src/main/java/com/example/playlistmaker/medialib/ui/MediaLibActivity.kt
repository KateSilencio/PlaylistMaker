package com.example.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Активация тулбара для окна Настройки
        val toolbar = binding.toolbarMediaLib
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewPager.adapter = MediaLibPageAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) {
                getString(R.string.favorite_tracks)
            } else {
                getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}