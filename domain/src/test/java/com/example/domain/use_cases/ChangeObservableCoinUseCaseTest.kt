package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test


class ChangeObservableCoinUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = ChangeObservableCoinUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        coEvery { repository.changeObservableCoin(any(), any()) } returns Unit

        useCase("1", true)

        coVerify(exactly = 1) { repository.changeObservableCoin(any(), any()) }
    }

    @Test
    fun `should call repository method with the same parameters`() = runBlocking {
        coEvery { repository.changeObservableCoin(any(), any()) } returns Unit

        val id = "1"
        val observable = true
        useCase(id, observable)

        coVerify(exactly = 1) { repository.changeObservableCoin(id, observable) }
    }

}