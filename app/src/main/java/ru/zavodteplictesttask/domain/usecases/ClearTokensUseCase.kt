package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Inject

interface ClearTokensUseCase : FlowUseCase<Unit, ApiResult<Unit>>

class ClearTokensUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : ClearTokensUseCase {

    override fun execute(param: Unit): Flow<ApiResult<Unit>> = flow {
        emit(repository.clearTokens())
    }
}