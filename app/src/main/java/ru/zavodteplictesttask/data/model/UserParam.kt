package ru.zavodteplictesttask.data.model

import ru.zavodteplictesttask.data.local.entity.UserEntity
import ru.zavodteplictesttask.data.remote.request.AvatarRequest
import ru.zavodteplictesttask.data.remote.request.UserRequest

data class UserParam(
    val name: String? = "",
    val username: String = "",
    val birthday: String? = "",
    val city: String? = "",
    val vk: String? = "",
    val instagram: String? = "",
    val about: String? = "",
    val phone: String = "",
    val userId: Int = 0,
    val avatar: String? = "",
    val avatars: AvatarParam? = null
) {
    fun toUserRequest(): UserRequest {
        return UserRequest(
            name = name,
            username = username,
            birthday = birthday,
            city = city,
            vk = vk,
            instagram = instagram,
            avatar = AvatarRequest(
                filename = avatars?.filename,
                base64 = avatars?.base64
            )
        )
    }

    fun toUserEntity(): UserEntity {
        return UserEntity(
            name = name,
            phone = phone,
            username = username,
            city = city,
            birthday = birthday,
            id = userId,
            avatar = avatar,
            vk = vk,
            instagram = instagram
        )
    }
}


data class AvatarParam(
    val filename: String?,
    val base64: String?
)