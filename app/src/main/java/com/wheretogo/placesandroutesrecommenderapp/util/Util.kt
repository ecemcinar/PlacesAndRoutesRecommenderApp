package com.wheretogo.placesandroutesrecommenderapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Util {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = currentDateTime.format(formatter)

        return "$time ${currentDateTime.dayOfMonth}/${currentDateTime.monthValue}/${currentDateTime.year}"
    }
}