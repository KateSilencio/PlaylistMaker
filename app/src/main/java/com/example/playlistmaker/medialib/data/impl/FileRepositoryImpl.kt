package com.example.playlistmaker.medialib.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.playlistmaker.medialib.domain.filestore.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileRepositoryImpl (private val context: Context) : FileRepository {
    override suspend fun saveCoverImage(uri: Uri): String? =
        withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {
                val fileName = "cover_playlist_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)

                //входящий поток из выбр картинки
                inputStream = context.contentResolver.openInputStream(uri)
                //исход поток в созд файл
                outputStream = FileOutputStream(file)
                BitmapFactory
                    .decodeStream(inputStream)
                    //сжимаем файл и копируем в поток
                    .compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

                //метод, если не сжимаем картинку:
                //inputStream?.copyTo(outputStream)

                file.absolutePath
            } catch (e: Exception) {
                Log.e("ERROR", "Error saving image cover", e)
                null
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }

    override suspend fun deleteImage(path: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}