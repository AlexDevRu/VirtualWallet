package com.example.domain.use_cases.get_local_coin

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetLocalCoinByIdFlowUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(id: String) = repository.getCoinByIdFlow(id)
}