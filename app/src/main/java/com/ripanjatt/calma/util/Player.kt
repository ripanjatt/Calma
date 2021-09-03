package com.ripanjatt.calma.util

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import com.ripanjatt.calma.services.AudioService
import com.ripanjatt.calma.services.Notifier
import kotlin.concurrent.thread

object Player {

    private val player = MediaPlayer()
    private var progressListener: ProgressListener? = null
    var isPlaying = false
    var isPaused = false

    fun setProgressListener(progressListener: ProgressListener) {
        this.progressListener = progressListener
        progress()
    }

    fun play(context: Context, audio: Audio) {
        isPlaying = true
        isPaused = false
        context.stopService(Intent(context, AudioService::class.java))
        if(player.isPlaying) player.stop()
        player.reset()
        player.setDataSource(context, Uri.parse(audio.uri))
        player.prepare()
        context.startService(Intent(context, AudioService::class.java))
        Notifier.showNotification(context, audio)
    }

    fun pause(audio: Audio, context: Context) {
        isPlaying = false
        isPaused = true
        player.pause()
        context.stopService(Intent(context, AudioService::class.java))
        Notifier.showNotification(context, audio)
    }

    fun resume(audio: Audio, context: Context) {
        isPlaying = true
        isPaused = false
        context.startService(Intent(context, AudioService::class.java))
        Notifier.showNotification(context, audio)
    }

    fun start() {
        player.start()
    }

    fun seekTo(int: Int) {
        thread {
            player.seekTo(int)
        }
    }

    private fun progress() {
        thread {
            while(true) {
                Thread.sleep(500)
                if(isPlaying) {
                    progressListener?.progress(player.currentPosition)
                }
            }
        }
        player.setOnCompletionListener {
            progressListener?.onComplete()
        }
    }

    interface ProgressListener {
        fun progress(progress: Int)
        fun onComplete()
    }
}