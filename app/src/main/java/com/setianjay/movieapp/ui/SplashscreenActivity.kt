package com.setianjay.movieapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.setianjay.movieapp.R


class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar!!.hide()
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(applicationContext,
                MainActivity::class.java))
            finish()
        },3000)
    }
}