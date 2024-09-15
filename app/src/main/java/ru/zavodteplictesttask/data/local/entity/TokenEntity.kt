package ru.zavodteplictesttask.data.local.entity

data class TokenEntity(
    val refreshToken: String?,
    val accessToken: String?,
    val userId: Int = 0,
    val isUserExists: Boolean = false,
    val tokenTime: Long? = null
)