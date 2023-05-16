package com.example.domain.use_cases.get_all_coins

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetAllCoinsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() = repository.getAllCoins()
}