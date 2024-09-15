package ru.zavodteplictesttask.data.remote.api

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zavodteplictesttask.data.remote.dto.RefreshTokenDto
import ru.zavodteplictesttask.data.remote.request.RefreshTokenRequest


interface TokenApi {
    @POST("refresh-token/")
    suspend fun refreshTokenAsync(
        @Body request: RefreshTokenRequest
    ): RefreshTokenDto
}