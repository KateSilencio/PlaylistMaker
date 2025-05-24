package com.example.playlistmaker.medialib.domain.filestore

import android.net.Uri

interface FileRepository {

    suspend fun saveCoverImage(uri: Uri): String?

    suspend fun deleteImage(path: String): Boolean
}