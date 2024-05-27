package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backward_btn_id = findViewById<ImageButton>(R.id.backward_btn)

        backward_btn_id.setOnClickListener{
            val backwardIntent = Intent(this, MainActivity::class.java)
            startActivity(backwardIntent)
        }
    }
}