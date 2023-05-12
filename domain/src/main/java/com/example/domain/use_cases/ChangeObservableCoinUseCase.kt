package com.example.domain.use_cases

import com.example.domain.repositories.Repository
import javax.inject.Inject

class ChangeObservableCoinUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: String, observable: Boolean) =
        repository.changeObservableCoin(id, observable)
}