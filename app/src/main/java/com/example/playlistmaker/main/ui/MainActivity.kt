package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.bottomNavigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)

        // Cлушатель для управления отступами
        binding.mainFragmentContainerView.setOnApplyWindowInsetsListener { gap, insets ->

            val bottomNavHeight = bottomNavView.height
            //отступ снизу для контейнера фрагментов
            gap.setPadding(0, 0, 0, bottomNavHeight)
            insets
        }
    }
}