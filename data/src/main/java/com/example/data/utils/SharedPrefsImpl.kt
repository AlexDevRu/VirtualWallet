package com.example.data.utils

import android.content.Context
import androidx.core.content.edit
import com.example.domain.utils.SharedPrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SharedPrefs {

    companion object {
        const val SELECTED_COIN_ID = "SELECTED_COIN_ID"
        const val COIN_AMOUNT = "COIN_AMOUNT"
    }

    private val preferences by lazy {
        context.getSharedPreferences("VirtualWallet", Context.MODE_PRIVATE)
    }

    override var selectedCoinId: String?
        get() = preferences.getString(SELECTED_COIN_ID, null)
        set(value) = preferences.edit { putString(SELECTED_COIN_ID, value) }

    override var coinAmount: Float
        get() = preferences.getFloat(COIN_AMOUNT, 0f)
        set(value) = preferences.edit { putFloat(COIN_AMOUNT, value) }
}
