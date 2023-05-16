package com.example.learning_android_virtualwallet_kulakov.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import com.example.learning_android_virtualwallet_kulakov.R
import com.example.learning_android_virtualwallet_kulakov.databinding.ActivityWidgetConfigurationBinding
import com.example.learning_android_virtualwallet_kulakov.ui.Utils

class AppWidgetConfigurationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWidgetConfigurationBinding

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWidgetConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        binding.btnConfirm.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnConfirm -> confirm()
            binding.btnCancel -> finish()
        }
    }

    private fun confirm() {
        val interval = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rb6 -> 6L
            R.id.rb12 -> 12L
            else -> 24L
        }
        Utils.startWorker(applicationContext, ExistingPeriodicWorkPolicy.UPDATE, interval, 0)
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}