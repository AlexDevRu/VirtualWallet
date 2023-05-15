package com.example.learning_android_virtualwallet_kulakov.ui

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun formatDate(date: Long) : String {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

}