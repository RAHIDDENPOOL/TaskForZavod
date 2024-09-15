package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.Register
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.AuthRepository
import javax.inject.Inject

data class RegisterParam(var username: String = "", var name: String = "", var phone: String = "")

interface RegisterUseCase : FlowUseCase<RegisterParam, ApiResult<Register>>

class RegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : RegisterUseCase {

    override fun execute(param: RegisterParam): Flow<ApiResult<Register>> = flow {
        emit(
            repository.register(
                phone = param.phone,
                name = param.name,
                username = param.username
            )
        )
    }

}