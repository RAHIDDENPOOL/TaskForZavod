package ru.zavodteplictesttask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AvatarsDto(
    @SerializedName("avatars")
    val avatars: AvatarDto?
)

data class AvatarDto(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("bigAvatar")
    val bigAvatar: String,
    @SerializedName("miniAvatar")
    val miniAvatar: String
)