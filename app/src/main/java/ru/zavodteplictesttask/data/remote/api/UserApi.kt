package ru.zavodteplictesttask.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.zavodteplictesttask.data.remote.dto.AvatarsDto
import ru.zavodteplictesttask.data.remote.dto.ProfileDto
import ru.zavodteplictesttask.data.remote.request.UserRequest

interface UserApi {
    @GET("me/")
    suspend fun getCurrentUser(): Response<ProfileDto>

    @PUT("me/")
    suspend fun updateUser(
        @Body request: UserRequest
    ): Response<AvatarsDto>
}