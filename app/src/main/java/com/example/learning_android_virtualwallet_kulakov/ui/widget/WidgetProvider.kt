package com.example.learning_android_virtualwallet_kulakov.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.example.domain.use_cases.GetLocalCoinByIdUseCase
import com.example.domain.utils.SharedPrefs
import com.example.learning_android_virtualwallet_kulakov.R
import com.example.learning_android_virtualwallet_kulakov.ui.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class WidgetProvider : AppWidgetProvider() {

    companion object {
        private const val REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
    }

    private fun getPenIntent(context: Context): PendingIntent? {
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = REFRESH_ACTION
        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @Inject
    lateinit var getLocalCoinByIdUseCase: GetLocalCoinByIdUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (context != null && intent?.action == REFRESH_ACTION) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    WidgetProvider::class.java
                )
            )
            appWidgetIds.forEach { updateWidget(context, appWidgetManager, it) }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun updateWidget(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context!!.packageName, R.layout.widget_layout)
        views.setTextViewText(R.id.tvLastUpdated, Utils.formatDate(System.currentTimeMillis()))

        val intent = Intent(context, AppWidgetConfigurationActivity::class.java)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }

        val currentCoin = runBlocking { getLocalCoinByIdUseCase(sharedPrefs.selectedCoinId.orEmpty()) }
        val symbol = currentCoin?.symbol ?: context.getString(R.string.usd)
        views.setTextViewText(R.id.tvCoinName, context.getString(R.string.coin_amount, sharedPrefs.coinAmount, symbol))

        val widgetTarget = AppWidgetTarget(context, R.id.ivCurrency, views, appWidgetId)
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(currentCoin?.getFullImageUrl())
            .error(R.drawable.ic_gold_coin)
            .into(widgetTarget)

        val configurationPendingIntent = PendingIntent.getActivity(context, 0, intent, flags)
        views.setOnClickPendingIntent(R.id.btnSettings, configurationPendingIntent)

        val listViewIntent = Intent(context, WidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
        views.setRemoteAdapter(R.id.lvCoins, listViewIntent)
        views.setEmptyView(R.id.lvCoins, R.id.emptyView)

        views.setOnClickPendingIntent(R.id.btnRefresh, getPenIntent(context))

        appWidgetManager?.updateAppWidget(appWidgetId, views)
    }

}