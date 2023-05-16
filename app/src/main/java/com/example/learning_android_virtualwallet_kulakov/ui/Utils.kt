package com.example.learning_android_virtualwallet_kulakov.ui

import android.content.Context
import androidx.work.*
import com.example.learning_android_virtualwallet_kulakov.work.ConvertWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {

    fun formatDate(date: Long) : String {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun startWorker(context: Context, existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy, repeatInterval: Long, initialDelayHours: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ConvertWorker>(repeatInterval, TimeUnit.HOURS).apply {
            if (initialDelayHours > 0)
                setInitialDelay(initialDelayHours, TimeUnit.HOURS)
        }.setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SYNC",
            existingPeriodicWorkPolicy,
            workRequest
        )
    }
}