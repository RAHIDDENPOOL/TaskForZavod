package ru.zavodteplictesttask.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.data.remote.api.UserApi
import ru.zavodteplictesttask.data.remote.dto.AvatarsDto
import ru.zavodteplictesttask.data.remote.dto.ProfileDto
import ru.zavodteplictesttask.data.remote.request.UserRequest
import ru.zavodteplictesttask.extensions.safeApiCall

class UserRemoteDataSource(
    private val api: UserApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchCurrentUser(): ApiResult<ProfileDto> = withContext(ioDispatcher) {
        safeApiCall {
            api.getCurrentUser()
        }
    }

    suspend fun updateUser(request: UserRequest): ApiResult<AvatarsDto> =
        withContext(ioDispatcher) {
            safeApiCall {
                api.updateUser(request)
            }
        }

}