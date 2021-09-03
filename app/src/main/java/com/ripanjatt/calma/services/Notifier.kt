package com.ripanjatt.calma.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ripanjatt.calma.R
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.util.Audio
import com.ripanjatt.calma.util.Player
import com.ripanjatt.calma.util.Util

object Notifier {

    fun showNotification(context: Context, audio: Audio) {
        val builder = NotificationCompat.Builder(context, "ripanjatt")
        builder.setSmallIcon(R.mipmap.music_icon)
        builder.setOngoing(Player.isPlaying)
        setInfo(builder, audio, context)
        setButtons(builder, context)
        builder.setStyle(androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(MediaSessionCompat(context, "tag").sessionToken))
        builder.setOnlyAlertOnce(true)
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        builder.setContentIntent(
            PendingIntent.getActivity(
                context,
            0,
            Intent(context, Home::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_CANCEL_CURRENT
        ))
        val manager = NotificationManagerCompat.from(context)
        manager.notify(8192, builder.build())
    }

    private fun setInfo(builder : NotificationCompat.Builder, audio: Audio, context: Context) {
        try {
            val bitmap = Util.getBitmap(context, Uri.parse(audio.uri), audio.albumID)
            builder.setLargeIcon(bitmap)
            builder.setContentTitle(audio.name)
            builder.setContentText(audio.artist)
        } catch (e : Exception) {
            Log.e("Error in setInfo", "" + e)
        }
    }

    private fun setButtons(builder: NotificationCompat.Builder, context: Context) {
        val prev = Intent(context, Receiver::class.java).setAction("prev")
        val play = Intent(context, Receiver::class.java).setAction("play")
        val next = Intent(context, Receiver::class.java).setAction("next")
        val pPrev = PendingIntent.getBroadcast(
            context, 0, prev, PendingIntent.FLAG_UPDATE_CURRENT)
        val pPlay = PendingIntent.getBroadcast(
            context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT)
        val pNext = PendingIntent.getBroadcast(
            context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.addAction(R.drawable.ic_baseline_skip_previous_24, "Prev", pPrev)
        if(Player.isPlaying) {
            builder.addAction(R.drawable.ic_baseline_pause_24, "Pause", pPlay)
        } else {
            builder.addAction(R.drawable.ic_baseline_play_arrow_24, "Play", pPlay)
        }
        builder.addAction(R.drawable.ic_baseline_skip_next_24, "Next", pNext)
    }

    fun createChannel(context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Calma"
            val desc = "Notification Channel for Calma"
            val channel = NotificationChannel("ripanjatt", name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = desc
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}