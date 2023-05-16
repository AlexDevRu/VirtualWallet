package com.example.domain.use_cases.get_local_coin

import com.example.domain.repositories.Repository
import javax.inject.Inject

class GetLocalCoinByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: String) = repository.getCoinById(id)
}
