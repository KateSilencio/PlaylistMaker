package com.example.playlistmaker.medialib.data.impl.shareplaylist

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.medialib.domain.api.shareplaylist.PlaylistShareNavigationRepository

class PlaylistShareNavigationRepositoryImpl(private val context: Context):
    PlaylistShareNavigationRepository {
    override fun sharePlaylist(text: String) {

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }

            val chooserIntent = Intent.createChooser(
                shareIntent,
                null
            ).apply {
                //флаг  новое задание было создано
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            //есть ли доступные приложения для intent
            if (chooserIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(chooserIntent)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.no_apps_available),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}