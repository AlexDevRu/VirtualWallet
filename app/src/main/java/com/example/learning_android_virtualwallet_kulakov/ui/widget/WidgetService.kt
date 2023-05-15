package com.example.learning_android_virtualwallet_kulakov.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.domain.models.Coin
import com.example.domain.use_cases.GetLocalCoinByIdUseCase
import com.example.domain.use_cases.GetObservableCoinsUseCase
import com.example.domain.utils.SharedPrefs
import com.example.learning_android_virtualwallet_kulakov.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class WidgetService : RemoteViewsService() {

    @Inject
    lateinit var getObservableCoinsUseCase: GetObservableCoinsUseCase

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @Inject
    lateinit var getLocalCoinByIdUseCase: GetLocalCoinByIdUseCase

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return WidgetItemFactory(intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0) ?: 0)
    }

    inner class WidgetItemFactory(private val appWidgetId: Int): RemoteViewsFactory {

        private lateinit var coins: List<Coin>
        private var currentCoin: Coin? = null
        private var amount = 0f

        override fun onCreate() {
            coins = runBlocking { getObservableCoinsUseCase() }
        }

        override fun onDataSetChanged() {
            coins = runBlocking { getObservableCoinsUseCase() }
            amount = sharedPrefs.coinAmount
            val id = sharedPrefs.selectedCoinId
            currentCoin = runBlocking { if (id.isNullOrBlank()) null else getLocalCoinByIdUseCase(id) }
        }

        override fun onDestroy() {

        }

        override fun getCount(): Int {
            return coins.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(packageName, R.layout.list_item_currency_widget)

            val coin = coins[position]
            views.setTextViewText(R.id.tvCoinName, coin.fullName)

            val bitmap = Glide.with(applicationContext)
                .asBitmap()
                .load(coin.getFullImageUrl())
                .submit(40, 40)
                .get()
            views.setImageViewBitmap(R.id.ivCurrency, bitmap)

            val currentSumInUsd1 = amount * (currentCoin?.cryptoComparePrice?.toFloat() ?: 1f)

            val cryptoComparePrice = currentSumInUsd1 / coin.cryptoComparePrice
            val coinCapPrice = currentSumInUsd1 / coin.coinCapPrice
            views.setTextViewText(R.id.tvCryptoComparePrice, String.format("%.2f", cryptoComparePrice))
            views.setTextViewText(R.id.tvCoinCapPrice, String.format("%.2f", coinCapPrice))

            return views
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}
