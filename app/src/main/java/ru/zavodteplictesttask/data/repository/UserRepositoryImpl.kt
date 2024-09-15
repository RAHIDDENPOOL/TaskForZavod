package ru.zavodteplictesttask.data.repository

import android.util.Log
import ru.zavodteplictesttask.data.local.LocalDataSource
import ru.zavodteplictesttask.data.model.Avatar
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.model.UserParam
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.UserRemoteDataSource
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.data.remote.dto.AvatarDto
import ru.zavodteplictesttask.domain.mapper.asEntity
import ru.zavodteplictesttask.domain.mapper.asExternalModel
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getAndSyncCurrentUser(): ApiResult<Profile> {
        val userEntity = localDataSource.getSavedUser()
        return if (userEntity.id != 0 && userEntity.phone.isNotBlank() && userEntity.username.isNotBlank()) {
            ApiResult.Success(userEntity.asExternalModel())
        } else {
            when (val result = remoteDataSource.fetchCurrentUser()) {
                is ApiResult.Success -> {
                    val entity = result.data.profileData.asEntity()
                    localDataSource.saveAllUserData(entity)
                    ApiResult.Success(entity.asExternalModel())
                }

                is ApiResult.Error -> {
                    ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
                }
            }
        }
    }

    override suspend fun getCurrentUser(): ApiResult<Profile> {
        return when (val result = remoteDataSource.fetchCurrentUser()) {
            is ApiResult.Success -> {
                val entity = result.data.profileData.asEntity()
                localDataSource.saveAllUserData(entity)
                ApiResult.Success(entity.asExternalModel())
            }

            is ApiResult.Error ->
                ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
        }
    }

    override suspend fun updateUser(param: UserParam): ApiResult<Avatar> {
        return when (val result = remoteDataSource.updateUser(param.toUserRequest())) {
            is ApiResult.Success -> {
                val avatars = result.data.avatars
                val avatar = avatars?.let {
                    avatars.asExternalModel()
                } ?: AvatarDto("", "", "").asExternalModel()
                localDataSource.saveAllUserData(param.toUserEntity())
                ApiResult.Success(avatar)
            }

            is ApiResult.Error ->
                ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
        }
    }

    override suspend fun getSavedUser(): ApiResult<Profile> {
        return ApiResult.Success(localDataSource.getSavedUser().asExternalModel())
    }

    override suspend fun getTokens(): ApiResult<UserToken> {
        return ApiResult.Success(localDataSource.getSavedTokens().asExternalModel())
    }

    override suspend fun clearTokens(): ApiResult<Unit> {
        return ApiResult.Success(localDataSource.clearTokens())
    }
}