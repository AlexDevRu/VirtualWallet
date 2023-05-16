package com.example.domain.use_cases.get_all_coins

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetAllLocalCoinsFlowUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(query: String) = repository.getAllCoinsFlow(query)
}