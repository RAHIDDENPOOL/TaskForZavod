package ru.zavodteplictesttask.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zavodteplictesttask.data.remote.dto.PhoneDto
import ru.zavodteplictesttask.data.remote.dto.PhoneWithCodeDto
import ru.zavodteplictesttask.data.remote.dto.RegisterDto
import ru.zavodteplictesttask.data.remote.request.PhoneRequest
import ru.zavodteplictesttask.data.remote.request.PhoneWithCodeRequest
import ru.zavodteplictesttask.data.remote.request.RegisterRequest

interface AuthApi {
    @POST("send-auth-code/")
    suspend fun sendAuthCode(
        @Body request: PhoneRequest
    ): Response<PhoneDto>

    @POST("check-auth-code/")
    suspend fun checkAuthCode(
        @Body request: PhoneWithCodeRequest
    ): Response<PhoneWithCodeDto>

    @POST("register/")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterDto>
}