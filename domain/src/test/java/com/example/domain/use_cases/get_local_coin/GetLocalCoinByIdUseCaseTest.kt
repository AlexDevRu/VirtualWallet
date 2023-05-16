package com.example.domain.use_cases.get_local_coin

import com.example.domain.TestUtils
import com.example.domain.repositories.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetLocalCoinByIdUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = GetLocalCoinByIdUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        coEvery { repository.getCoinById(any()) } returns null

        useCase("1")

        coVerify(exactly = 1) { repository.getCoinById(any()) }
    }

    @Test
    fun `should call repository method with the same parameters`() = runBlocking {
        coEvery { repository.getCoinById(any()) } returns null

        val id = "1"
        useCase(id)

        coVerify(exactly = 1) { repository.getCoinById(id) }
    }

    @Test
    fun `should return the same data as repository method`() = runBlocking {
        val expected = TestUtils.createFakeCoin("1")

        coEvery { repository.getCoinById(any()) } returns expected

        val actual = useCase("1")

        Assert.assertEquals(expected, actual)
    }

}