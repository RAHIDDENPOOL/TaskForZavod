package ru.zavodteplictesttask.domain.repository

import ru.zavodteplictesttask.data.model.Avatar
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.model.UserParam
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult

interface UserRepository {
    suspend fun getAndSyncCurrentUser(): ApiResult<Profile>
    suspend fun getCurrentUser(): ApiResult<Profile>
    suspend fun updateUser(param: UserParam): ApiResult<Avatar>
    suspend fun getSavedUser(): ApiResult<Profile>
    suspend fun getTokens(): ApiResult<UserToken>
    suspend fun clearTokens(): ApiResult<Unit>
}