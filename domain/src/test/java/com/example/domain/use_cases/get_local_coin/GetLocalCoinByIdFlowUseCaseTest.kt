package com.example.domain.use_cases.get_local_coin

import com.example.domain.TestUtils
import com.example.domain.repositories.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetLocalCoinByIdFlowUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = GetLocalCoinByIdFlowUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        every { repository.getCoinByIdFlow(any()) } returns emptyFlow()

        useCase("1")

        verify(exactly = 1) { repository.getCoinByIdFlow(any()) }
    }

    @Test
    fun `should call repository method with the same parameters`() = runBlocking {
        every { repository.getCoinByIdFlow(any()) } returns emptyFlow()

        useCase("1")

        verify(exactly = 1) { repository.getCoinByIdFlow("1") }
    }

    @Test
    fun `should return the same data as repository method`() = runBlocking {
        val expected = flowOf(TestUtils.createFakeCoin("1"))

        every { repository.getCoinByIdFlow(any()) } returns expected

        val actual = useCase("1")

        Assert.assertEquals(expected, actual)
    }

}