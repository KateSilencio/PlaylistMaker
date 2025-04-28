package com.example.playlistmaker.medialib.domain.impl

import android.net.Uri
import com.example.playlistmaker.medialib.domain.filestore.FileInteractor
import com.example.playlistmaker.medialib.domain.filestore.FileRepository

class FileInteractorImpl(private val fileRepository: FileRepository): FileInteractor {
    override suspend fun saveImage(uri: Uri): String? {
        return fileRepository.saveCoverImage(uri)
    }
}