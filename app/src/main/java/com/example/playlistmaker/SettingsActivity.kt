package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.LinearLayout

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /*val backwardBtn = findViewById<ImageButton>(R.id.backward_settings_btn)
        backwardBtn.setOnClickListener {
            finish()
        }*/

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

        val layoutAgreement = findViewById<LinearLayout>(R.id.LayoutAgreement)
        layoutAgreement.setOnClickListener{
            val site = Uri.parse(getString(R.string.agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, site)
            if (agreementIntent.resolveActivity(packageManager)!=null){
                startActivity(agreementIntent)
            }
        }
    }
    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}