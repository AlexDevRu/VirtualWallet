package com.example.learning_android_virtualwallet_kulakov.ui.coin_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Coin
import com.example.domain.use_cases.GetAllCoinsUseCase
import com.example.learning_android_virtualwallet_kulakov.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase,
    app: Application
) : ViewModel() {

    private val usd = Coin(
        id = "",
        imageUrl = null,
        symbol = app.getString(R.string.usd),
        fullName = app.getString(R.string.usd),
        cryptoComparePrice = 1.0,
        coinCapPrice = 1.0,
        observable = false
    )

    private val _query = MutableStateFlow("")
    val query: String
        get() = _query.value

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val coins = _query.flatMapLatest {
        getAllCoinsUseCase.getFlow(it).map {
            val list = mutableListOf<Coin>()
            list.add(usd)
            list.addAll(it)
            list
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    init {
        viewModelScope.launch {
            _loading.emit(true)
            getAllCoinsUseCase()
            _loading.emit(false)
        }
    }

    fun setQuery(query: String?) {
        viewModelScope.launch { _query.emit(query.orEmpty()) }
    }

}