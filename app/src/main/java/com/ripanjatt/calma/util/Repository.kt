package com.ripanjatt.calma.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.ripanjatt.calma.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

object Repository {

    private var isRunning = true
    private var isImgCollected = false

    private val audioList = ArrayList<Audio>()
    private val albumMap = HashMap<String?, MutableList<Audio>>()
    private val artistMap = HashMap<String?, MutableList<Audio>>()
    private val bitmaps = HashMap<Long, Bitmap>()

    fun loadFiles(activity: Activity) {
        thread {
            if(Util.checkPermissions(activity)) {
                val temp = Data.getData(activity.applicationContext)
                if(temp.size == 0) {
                    val audioReader = AudioReader()
                    audioReader.getAudioFiles(activity.applicationContext, audioList)
                    audioReader.setReaderListener(object: AudioReader.ReaderListener {
                        override fun loaded() {
                            updateMaps()
                            getAllBitmaps(activity.applicationContext)
                            isRunning = false
                        }
                    })
                } else {
                    audioList.clear()
                    audioList.addAll(temp)
                    updateMaps()
                    isRunning = false
                    AudioReader().getAudioFiles(activity.applicationContext, ArrayList())
                    getAllBitmaps(activity.applicationContext)
                }
            }
        }
    }

    fun getAudio(): ArrayList<Audio> {
        while (isRunning) {
            //wait!
        }
        return audioList
    }

    fun getAlbums(): SortedMap<String?, MutableList<Audio>> {
        while (isRunning) {
            //wait!
        }
        return albumMap.toSortedMap(compareBy { it.toString().uppercase() })
    }

    fun getArtists(): SortedMap<String?, MutableList<Audio>> {
        while (isRunning) {
            //wait!
        }
        return artistMap.toSortedMap(compareBy { it.toString().uppercase() })
    }

    fun getFav(context: Context): ArrayList<Audio> {
        return Data.getFav(context)
    }

    fun setBitmap(imageView: ImageView, albumID: Long) {
        if(isImgCollected) {
            imageView.setImageBitmap(bitmaps[albumID])
        } else {
            imageView.setImageResource(R.mipmap.music_icon)
            CoroutineScope(Dispatchers.Default).launch {
                while(!isImgCollected) {
                    //wait!
                }
                withContext(Main) {
                    imageView.setImageBitmap(bitmaps[albumID])
                }
            }
        }
    }

    private fun getAllBitmaps(context: Context) {
        bitmaps.clear()
        thread {
            albumMap.forEach { (_, mutableList) ->
                val it = mutableList[0]
                bitmaps[it.albumID] = Util.getBitmap(context, Uri.parse(it.uri), it.albumID)
            }
            isImgCollected = true
        }
    }

    private fun updateMaps() {
        albumMap.clear()
        artistMap.clear()
        albumMap.putAll(audioList.stream().collect(Collectors.groupingBy { it.album }))
        artistMap.putAll(audioList.stream().collect(Collectors.groupingBy { it.artist }))
    }
}