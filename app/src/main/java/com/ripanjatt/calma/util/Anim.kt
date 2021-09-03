package com.ripanjatt.calma.util

import android.app.Activity
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.DisplayMetrics
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

object Anim {

    fun showAnim(view: View, home: View, activity: Activity) {
        thread {
            val height = if(SDK_INT >= Build.VERSION_CODES.R) {
                activity.windowManager.currentWindowMetrics.bounds.height()
            } else {
                val metrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(metrics)
                metrics.heightPixels
            }
            CoroutineScope(Main).launch {
                view.translationY = height.toFloat()
                view.visibility = View.VISIBLE
            }
            var i = height.toFloat()
            while (i >= 0) {
                Thread.sleep(5)
                CoroutineScope(Main).launch {
                    view.translationY = i
                }
                i -= 50
            }
            CoroutineScope(Main).launch {
                home.visibility = View.GONE
                view.translationY = 0f
            }
        }
    }

    fun hideAnim(view: View, home: View, activity: Activity) {
        home.visibility = View.VISIBLE
        thread {
            val height = if(SDK_INT >= Build.VERSION_CODES.R) {
                activity.windowManager.currentWindowMetrics.bounds.height()
            } else {
                val metrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(metrics)
                metrics.heightPixels
            }
            var i = 0f
            while (i <= height) {
                Thread.sleep(5)
                CoroutineScope(Main).launch {
                    view.translationY = i
                }
                i += 50
            }
            CoroutineScope(Main).launch {
                view.translationY = height.toFloat()
                view.visibility = View.GONE
            }
        }
    }
}