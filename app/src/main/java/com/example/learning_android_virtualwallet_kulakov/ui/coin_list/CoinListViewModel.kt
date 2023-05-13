package com.example.learning_android_virtualwallet_kulakov.ui.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Coin
import com.example.domain.use_cases.GetAllCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase
) : ViewModel() {

    private val _coins = MutableStateFlow<List<Coin>>(emptyList())

    private val _query = MutableStateFlow("")
    val query: String
        get() = _query.value

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val coins = _query.flatMapLatest {
        getAllCoinsUseCase.getFlow(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    init {
        viewModelScope.launch {
            _loading.emit(true)
            _coins.emit(getAllCoinsUseCase())
            _loading.emit(false)
        }
    }

    fun setQuery(query: String?) {
        viewModelScope.launch { _query.emit(query.orEmpty()) }
    }

}