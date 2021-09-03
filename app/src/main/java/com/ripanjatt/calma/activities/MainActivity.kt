package com.ripanjatt.calma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ripanjatt.calma.R
import com.ripanjatt.calma.services.Notifier
import com.ripanjatt.calma.util.Repository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Repository.loadFiles(this)
        Notifier.createChannel(this)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Home::class.java))
            this.finish()
        }
        , 2000)
    }
}