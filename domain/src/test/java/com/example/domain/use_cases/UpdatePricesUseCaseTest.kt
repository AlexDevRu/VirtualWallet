package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UpdatePricesUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = UpdatePricesUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        coEvery { repository.updatePrices(any()) } returns Unit

        useCase("1")

        coVerify(exactly = 1) { repository.updatePrices(any()) }
    }

    @Test
    fun `should call repository method with the same parameters`() = runBlocking {
        coEvery { repository.updatePrices(any()) } returns Unit

        val id = "1"
        useCase(id)

        coVerify(exactly = 1) { repository.updatePrices(id) }
    }

}