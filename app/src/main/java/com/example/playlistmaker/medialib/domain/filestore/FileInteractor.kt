package com.example.playlistmaker.medialib.domain.filestore

import android.net.Uri

interface FileInteractor {

    suspend fun saveImage(uri: Uri): String?

    suspend fun deleteImage(path: String): Boolean
}