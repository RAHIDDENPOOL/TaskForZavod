package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.PhoneSuccess
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.AuthRepository
import javax.inject.Inject

data class PhoneParam(val phone: String)

interface SendAuthCodeUseCase : FlowUseCase<PhoneParam, ApiResult<PhoneSuccess>>

class SendAuthCodeUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : SendAuthCodeUseCase {

    override fun execute(param: PhoneParam): Flow<ApiResult<PhoneSuccess>> = flow {
        emit(repository.sendAuthCode(param.phone))
    }

}