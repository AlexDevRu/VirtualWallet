package com.example.domain.use_cases

import com.example.domain.TestUtils
import com.example.domain.repositories.Repository
import com.example.domain.use_cases.get_all_coins.GetAllCoinsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetObservableCoinsUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = GetObservableCoinsUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        coEvery { repository.getObservableCoins() } returns emptyList()

        useCase()

        coVerify(exactly = 1) { repository.getObservableCoins() }
    }

    @Test
    fun `should return the same data as repository method`() = runBlocking {
        val expected = List(10) { TestUtils.createFakeCoin(it.toString()) }

        coEvery { repository.getObservableCoins() } returns expected

        val actual = useCase()

        Assert.assertEquals(expected, actual)
    }

}