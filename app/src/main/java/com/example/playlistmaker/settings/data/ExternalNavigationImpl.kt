package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.ExternalNavigation

class ExternalNavigationImpl(private val context: Context): ExternalNavigation {
    override fun shareLink(link: String) {

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val shareChooserIntent = Intent.createChooser(shareIntent, null).apply {
            // Добавляем флаг к chooser Intent
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (shareChooserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareChooserIntent)
        }
    }

    override fun emailSupport(email: String, subject: String, text: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT,subject)
            putExtra(Intent.EXTRA_TEXT,text)
            //контекст не является активностью - добавляем флаг
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openAgreementLink(url: String) {
        val site = Uri.parse(url)
        val agreementIntent = Intent(Intent.ACTION_VIEW, site).apply {
            //контекст не является активностью - добавляем флаг
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (agreementIntent.resolveActivity(context.packageManager)!=null){
            context.startActivity(agreementIntent)
        } else {
            Toast.makeText(context, R.string.no_apps_available, Toast.LENGTH_SHORT).show()
        }
    }
}