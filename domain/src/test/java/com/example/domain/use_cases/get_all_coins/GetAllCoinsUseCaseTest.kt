package com.example.domain.use_cases.get_all_coins

import com.example.commontest.TestUtils
import com.example.domain.repositories.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetAllCoinsUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = GetAllCoinsUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        coEvery { repository.getAllCoins() } returns emptyList()

        useCase()

        coVerify(exactly = 1) { repository.getAllCoins() }
    }

    @Test
    fun `should return the same data as repository method`() = runBlocking {
        val expected = List(10) { TestUtils.createFakeCoin(it.toString()) }

        coEvery { repository.getAllCoins() } returns expected

        val actual = useCase()

        Assert.assertEquals(expected, actual)
    }

}