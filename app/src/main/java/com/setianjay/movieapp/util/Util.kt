package com.setianjay.movieapp.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun formatDate(date: String): String{
        if (date.isNullOrBlank()){
            return Log.d("Util","Data null").toString()
        }
        val formatter = SimpleDateFormat("MMM dd, yyyy").parse(date)
        return formatter.toString()
    }
}