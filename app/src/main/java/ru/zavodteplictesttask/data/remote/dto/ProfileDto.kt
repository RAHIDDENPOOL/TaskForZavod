package ru.zavodteplictesttask.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("profile_data")
    val profileData: UserDto
)