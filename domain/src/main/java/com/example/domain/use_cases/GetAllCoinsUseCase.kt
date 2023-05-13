package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetAllCoinsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() = repository.getAllCoins()
    fun getFlow(query: String) = repository.getAllCoinsFlow(query)
}