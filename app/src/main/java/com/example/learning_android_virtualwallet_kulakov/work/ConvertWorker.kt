package com.example.learning_android_virtualwallet_kulakov.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.data_sources.CoinCapDataSource
import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.use_cases.GetObservableCoinsFlowUseCase
import com.example.domain.utils.SharedPrefs
import com.example.learning_android_virtualwallet_kulakov.ui.widget.WidgetProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.last

@HiltWorker
class ConvertWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sharedPrefs: SharedPrefs,
    private val getObservableCoinsFlowUseCase: GetObservableCoinsFlowUseCase,
    private val cryptoCompareDataSource: CryptoCompareDataSource,
    private val coinCapDataSource: CoinCapDataSource,
    private val localDataSource: LocalDataSource
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val observableCoins = getObservableCoinsFlowUseCase().last()
        observableCoins.forEach {
            val priceInUsd1 = cryptoCompareDataSource.getPriceInUsd(it.symbol)
            val priceInUsd2 = coinCapDataSource.getPriceInUsd(it.symbol)
            localDataSource.updatePrice(it.id, priceInUsd1, priceInUsd2)
        }
        
        val selectedCoinId = sharedPrefs.selectedCoinId
        if (!selectedCoinId.isNullOrBlank()) {
            val coin = localDataSource.getCoinById(selectedCoinId)
            if (coin != null) {
                val priceInUsd1 = cryptoCompareDataSource.getPriceInUsd(coin.symbol)
                val priceInUsd2 = coinCapDataSource.getPriceInUsd(coin.symbol)
                localDataSource.updatePrice(coin.id, priceInUsd1, priceInUsd2)
            }
        }

        val refreshIntent = WidgetProvider.getRefreshIntent(applicationContext)
        applicationContext.sendBroadcast(refreshIntent)

        return Result.success()
    }

}