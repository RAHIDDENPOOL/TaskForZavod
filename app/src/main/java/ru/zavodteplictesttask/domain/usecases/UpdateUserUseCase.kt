package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.Avatar
import ru.zavodteplictesttask.data.model.UserParam
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Inject

interface UpdateUserUseCase : FlowUseCase<UserParam, ApiResult<Avatar>>

class UpdateUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : UpdateUserUseCase {

    override fun execute(param: UserParam): Flow<ApiResult<Avatar>> = flow {
        emit(repository.updateUser(param))
    }
}