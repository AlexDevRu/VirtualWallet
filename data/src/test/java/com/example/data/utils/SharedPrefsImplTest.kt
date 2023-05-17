package com.example.data.utils

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SharedPrefsImplTest {

    private val appContext = RuntimeEnvironment.getApplication()
    private val sharedPrefsImpl = SharedPrefsImpl(appContext)

    private val prefs = appContext.getSharedPreferences("VirtualWallet", Context.MODE_PRIVATE)

    @Test
    fun `selectedCoinId property saves value in shared preferences with correct key`() {
        val expected = "A"
        sharedPrefsImpl.selectedCoinId = expected
        val actual = prefs.getString(SharedPrefsImpl.SELECTED_COIN_ID, null)
        assertEquals(expected, actual)
    }

    @Test
    fun `coinAmount property saves value in shared preferences with correct key`() {
        val expected = 12f
        sharedPrefsImpl.coinAmount = expected
        val actual = prefs.getFloat(SharedPrefsImpl.COIN_AMOUNT, 0f)
        assertEquals(expected, actual)
    }

    @Test
    fun `selectedCoinId property returns value which is saved in shared preferences`() {
        val expected = "A"
        prefs.edit().putString(SharedPrefsImpl.SELECTED_COIN_ID, expected).commit()
        val actual = sharedPrefsImpl.selectedCoinId
        assertEquals(expected, actual)
    }

    @Test
    fun `coinAmount property returns value which is saved in shared preferences`() {
        val expected = 12f
        prefs.edit().putFloat(SharedPrefsImpl.COIN_AMOUNT, expected).commit()
        val actual = sharedPrefsImpl.coinAmount
        assertEquals(expected, actual)
    }
}