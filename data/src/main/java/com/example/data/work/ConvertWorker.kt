package com.example.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.use_cases.GetObservableCoinsFlowUseCase
import com.example.domain.utils.SharedPrefs
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
    private val localDataSource: LocalDataSource
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val observableCoins = getObservableCoinsFlowUseCase().last()
        observableCoins.forEach {
            val priceInUsd = cryptoCompareDataSource.getPriceInUsd(it.symbol)
            localDataSource.updatePrice(it.id, priceInUsd)
        }
        
        val selectedCoinId = sharedPrefs.selectedCoinId
        if (!selectedCoinId.isNullOrBlank()) {
            val coin = localDataSource.getCoinById(selectedCoinId)
            if (coin != null) {
                val priceInUsd = cryptoCompareDataSource.getPriceInUsd(coin.symbol)
                localDataSource.updatePrice(coin.id, priceInUsd)
            }
        }
        return Result.success()
    }

}