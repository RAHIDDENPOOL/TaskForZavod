package ru.zavodteplictesttask.domain.mapper

import ru.zavodteplictesttask.data.local.entity.TokenEntity
import ru.zavodteplictesttask.data.local.entity.UserAvatarEntity
import ru.zavodteplictesttask.data.local.entity.UserEntity
import ru.zavodteplictesttask.data.model.Avatar
import ru.zavodteplictesttask.data.model.PhoneSuccess
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.model.ProfileAvatar
import ru.zavodteplictesttask.data.model.Register
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.dto.AvatarDto
import ru.zavodteplictesttask.data.remote.dto.PhoneDto
import ru.zavodteplictesttask.data.remote.dto.PhoneWithCodeDto
import ru.zavodteplictesttask.data.remote.dto.RegisterDto
import ru.zavodteplictesttask.data.remote.dto.UserDto

fun PhoneDto.asExternalModel() = PhoneSuccess(isSuccess = isSuccess)

fun PhoneWithCodeDto.asEntity() =
    TokenEntity(
        userId = userId,
        refreshToken = refreshToken,
        accessToken = accessToken,
        isUserExists = isUserExists
    )

fun TokenEntity.asExternalModel() =
    UserToken(
        userId = userId,
        refreshToken = refreshToken ?: "",
        accessToken = accessToken ?: "",
        isUserExists = isUserExists
    )

fun RegisterDto.asEntity() =
    TokenEntity(
        userId = userId,
        refreshToken = refreshToken,
        accessToken = accessToken
    )

fun TokenEntity.asExternalModel2() =
    Register(
        userId = userId,
        refreshToken = refreshToken ?: "",
        accessToken = accessToken ?: ""
    )

fun UserDto.asEntity() = UserEntity(
    name = name,
    phone = phone,
    username = username,
    city = city,
    birthday = birthday,
    id = id,
    avatar = avatar,
    vk = vk,
    instagram = instagram,
    avatars = UserAvatarEntity(
        avatar = avatars?.avatar,
        bigAvatar = avatars?.bigAvatar,
        miniAvatar = avatars?.miniAvatar
    )
)

fun AvatarDto.asExternalModel() = Avatar(
    avatar = avatar,
    bigAvatar = bigAvatar,
    miniAvatar = miniAvatar
)

fun UserEntity.asExternalModel() = Profile(
    id = id,
    name = name,
    phone = phone,
    username = username,
    city = city,
    birthday = birthday,
    avatar = avatar,
    vk = vk,
    instagram = instagram,
    avatars = ProfileAvatar(avatar = avatars?.avatar)
)