package com.example.domain.use_cases

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

class GetObservableCoinsFlowUseCaseTest {

    private val repository = mockk<Repository>()
    private val useCase = GetObservableCoinsFlowUseCase(repository)

    @Test
    fun `should call repository method`() = runBlocking {
        every { repository.getObservableCoinsFlow() } returns emptyFlow()

        useCase()

        verify(exactly = 1) { repository.getObservableCoinsFlow() }
    }

    @Test
    fun `should return the same data as repository method`() = runBlocking {
        val expected = flowOf(List(10) { TestUtils.createFakeCoin(it.toString()) })

        every { repository.getObservableCoinsFlow() } returns expected

        val actual = useCase()

        Assert.assertEquals(expected, actual)
    }

}