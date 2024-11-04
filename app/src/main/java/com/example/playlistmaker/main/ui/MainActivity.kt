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

        if (savedInstanceState == null) {
            bottomNavView.selectedItemId = R.id.mediaLibNavViewItem
        }

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.searchNavViewItem -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.mediaLibNavViewItem -> {
                    navController.navigate(R.id.mediaLibFragment)
                    true
                }
                R.id.settingsNavViewItem -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }
}