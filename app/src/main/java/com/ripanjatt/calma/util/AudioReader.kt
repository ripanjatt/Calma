package com.ripanjatt.calma.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AudioReader {

    private var readerListener: ReaderListener? = null

    fun setReaderListener(readerListener: ReaderListener) {
        this.readerListener = readerListener
    }

    @SuppressLint("InlinedApi")
    fun getAudioFiles(context: Context, list: ArrayList<Audio>) {
        thread {
            try {
                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        MediaStore.Audio.Media.getContentUri(
                            MediaStore.VOLUME_EXTERNAL
                        )
                    } else {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM_ID
                )
                val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
                val selectionArgs = arrayOf(
                    TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES).toString()
                )
                val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
                val query = context.contentResolver.query(
                    collection,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )
                query?.use { cursor ->
                    // Cache column indices.
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val albumIDColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val album = cursor.getString(albumColumn)
                        val size = cursor.getInt(sizeColumn)
                        val duration = cursor.getLong(durationColumn)
                        val artist = cursor.getString(artistColumn)
                        val albumID = cursor.getLong(albumIDColumn)

                        val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        list.add(Audio(contentUri.toString(), name, duration, size, album, artist, albumID))
                    }
                    list.sortBy { it.name.uppercase() }
                    Data.setData(list, context)
                    readerListener?.loaded()
                }
            } catch (e: Exception) {
                Log.e("Error getAudioFiles", "$e")
            }
        }
    }

    interface ReaderListener {
        fun loaded()
    }
}