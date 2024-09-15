package ru.zavodteplictesttask.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.data.remote.api.AuthApi
import ru.zavodteplictesttask.data.remote.dto.PhoneDto
import ru.zavodteplictesttask.data.remote.dto.PhoneWithCodeDto
import ru.zavodteplictesttask.data.remote.dto.RegisterDto
import ru.zavodteplictesttask.data.remote.request.PhoneRequest
import ru.zavodteplictesttask.data.remote.request.PhoneWithCodeRequest
import ru.zavodteplictesttask.data.remote.request.RegisterRequest
import ru.zavodteplictesttask.extensions.safeApiCall


class AuthRemoteDataSource(
    private val api: AuthApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun sendAuthCode(phone: String): ApiResult<PhoneDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                api.sendAuthCode(PhoneRequest(phone))
            }
        }

    suspend fun checkAuthCode(phone: String, code: String): ApiResult<PhoneWithCodeDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                api.checkAuthCode(PhoneWithCodeRequest(phone = phone, code = code))
            }
        }

    suspend fun register(name: String, username: String, phone: String): ApiResult<RegisterDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                api.register(RegisterRequest(phone = phone, name = name, username = username))
            }
        }
}