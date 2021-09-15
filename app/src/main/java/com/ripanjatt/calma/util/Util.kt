package com.ripanjatt.calma.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ripanjatt.calma.R
import com.ripanjatt.calma.activities.Home
import java.text.DecimalFormat

object Util {

    fun getTime(longTime: Long): String {
        val time = ((longTime.toDouble() / 1000.0) / 60.0).toString().split(".")
        return (time[0] + DecimalFormat(".00").format(("." + time[1]).toDouble() * 0.6).toString())
    }

    @SuppressLint("Recycle")
    fun getBitmap(context: Context, uri: Uri, albumID: Long): Bitmap {
        var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.music_icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            bitmap = try {
                context.contentResolver.loadThumbnail(
                    uri, Size(512, 512), null)
            } catch(e: Exception) {
                BitmapFactory.decodeResource(context.resources, R.mipmap.music_icon)
            }
        } else {
            try {
                val artUri = ContentUris.withAppendedId(
                    Uri
                    .parse("content://media/external/audio/albumart"), albumID)
                val descriptor = context.contentResolver
                    .openFileDescriptor(artUri, "r")
                if(descriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(descriptor.fileDescriptor, null,
                        null)
                }
            } catch (e: Exception) {
            }
        }
        return bitmap
    }

    fun setPreferences(shuffle: Boolean, repeat: Boolean, context: Context) {
        val editor = context.getSharedPreferences("pref", Context.MODE_PRIVATE).edit()
        editor.putBoolean("shuffle", shuffle)
        editor.putBoolean("repeat", repeat)
        editor.apply()
    }

    fun getShuffle(context: Context): Boolean {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getBoolean("shuffle", false)
    }

    fun getRepeat(context: Context): Boolean {
        val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref.getBoolean("repeat", false)
    }

    fun permissionDialog(home: Home) {
        val builder = AlertDialog.Builder(home)
        builder.setTitle("Permissions")
        builder.setMessage("Storage read permission is required to access audio files.\nAllow now?")
        builder.setNegativeButton("No", null)
        builder.setPositiveButton("Yes") { _, _ ->
            getPermissions(home)
        }
        builder.show()
    }

    fun checkPermissions(activity: Activity) : Boolean {
        return ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPermissions(activity: Activity) {
        if (!checkPermissions(activity)
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                8192
            )
        }
    }
}