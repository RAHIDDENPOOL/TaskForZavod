package ru.zavodteplictesttask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_id")
    val userId: Int
)