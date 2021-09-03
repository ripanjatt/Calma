package com.ripanjatt.calma.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        BindingService.received(intent?.action.toString())
    }
}