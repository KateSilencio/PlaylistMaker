package com.example.playlistmaker.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharedprefs.SharedPrefFunRepository

class SettingsEventManager(
    private val context: Context,
    private val sharedPrefFunRepository: SharedPrefFunRepository
) {

    fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.android_developer_link))
            type = "text/plain"
        }
        val shareChooserIntent = Intent.createChooser(shareIntent, null)
        if (shareChooserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareChooserIntent)
        }
    }

    fun emailSupport(){
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.student_mail)))
            putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.topic_mail))
            putExtra(Intent.EXTRA_TEXT,context.getString(R.string.message_mail))
            context.startActivity(this)
        }
    }

    fun agreementLink(){
        val site = Uri.parse(context.getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, site)
        if (agreementIntent.resolveActivity(context.packageManager)!=null){
            context.startActivity(agreementIntent)
        } else {
            Toast.makeText(context,R.string.no_apps_available,Toast.LENGTH_SHORT).show()
        }
    }

    fun saveThemeSharedPref(isChecked: Boolean){
        sharedPrefFunRepository.saveBoolean(SWITCH_PREFERENCES, SWITCH_IS_CHECKED,isChecked)
    }
}