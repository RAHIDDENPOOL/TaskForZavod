package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Inject

interface FetchTokensUseCase : FlowUseCase<Unit, ApiResult<UserToken>>

class FetchTokensUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : FetchTokensUseCase {

    override fun execute(param: Unit): Flow<ApiResult<UserToken>> = flow {
        emit(repository.getTokens())
    }
}