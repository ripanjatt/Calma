package com.ripanjatt.calma.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ripanjatt.calma.util.Player

class AudioService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Player.start()
        return START_STICKY
    }
}