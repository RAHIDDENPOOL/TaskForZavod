package ru.zavodteplictesttask.domain.repository

import ru.zavodteplictesttask.data.model.PhoneSuccess
import ru.zavodteplictesttask.data.model.Register
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult

interface AuthRepository {
    suspend fun sendAuthCode(phone: String): ApiResult<PhoneSuccess>
    suspend fun checkAuthCode(phone: String, code: String): ApiResult<UserToken>
    suspend fun register(phone: String, name: String, username: String): ApiResult<Register>
}