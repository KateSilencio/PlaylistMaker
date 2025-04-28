package com.example.playlistmaker.medialib.domain.filestore

import android.net.Uri

interface FileInteractor {

    suspend fun saveImage(uri: Uri): String?
}