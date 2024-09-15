package ru.zavodteplictesttask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.repository.AuthRepository
import javax.inject.Inject

data class PhoneWithCodeParam(val phone: String, val code: String)

interface CheckAuthCodeUseCase : FlowUseCase<PhoneWithCodeParam, ApiResult<UserToken>>

class CheckAuthCodeUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : CheckAuthCodeUseCase {

    override fun execute(param: PhoneWithCodeParam): Flow<ApiResult<UserToken>> = flow {
        emit(repository.checkAuthCode(phone = param.phone, code = param.code))
    }
}