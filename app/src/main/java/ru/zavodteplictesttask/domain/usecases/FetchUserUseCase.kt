package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Inject

interface FetchUserUseCase : FlowUseCase<Boolean, ApiResult<Profile>>

class FetchUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : FetchUserUseCase {

    override fun execute(param: Boolean): Flow<ApiResult<Profile>> = flow {
        if (param) {
            emit(repository.getAndSyncCurrentUser())
        } else {
            emit(repository.getCurrentUser())
        }
    }

}