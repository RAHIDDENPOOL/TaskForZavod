package ru.zavodteplictesttask.data.repository

import ru.zavodteplictesttask.data.local.LocalDataSource
import ru.zavodteplictesttask.data.model.PhoneSuccess
import ru.zavodteplictesttask.data.model.Register
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.AuthRemoteDataSource
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.mapper.asEntity
import ru.zavodteplictesttask.domain.mapper.asExternalModel
import ru.zavodteplictesttask.domain.mapper.asExternalModel2
import ru.zavodteplictesttask.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendAuthCode(phone: String): ApiResult<PhoneSuccess> {
        return when (val result = remoteDataSource.sendAuthCode(phone)) {
            is ApiResult.Success -> {
                ApiResult.Success(result.data.asExternalModel())
            }

            is ApiResult.Error -> {
                ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
            }
        }
    }

    override suspend fun checkAuthCode(phone: String, code: String): ApiResult<UserToken> {
        return when (val result = remoteDataSource.checkAuthCode(phone, code)) {
            is ApiResult.Success -> {
                val entity = result.data.asEntity()
                if (entity.isUserExists) {
                    localDataSource.saveTokenData(
                        refreshToken = entity.refreshToken ?: "",
                        accessToken = entity.accessToken ?: "",
                        userId = entity.userId,
                        time = System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
                    )
                }
                ApiResult.Success(entity.asExternalModel())
            }

            is ApiResult.Error -> {
                ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
            }
        }
    }

    override suspend fun register(
        phone: String,
        name: String,
        username: String
    ): ApiResult<Register> {
        return when (val result =
            remoteDataSource.register(name = name, username = username, phone = phone)) {
            is ApiResult.Success -> {
                val entity = result.data.asEntity()
                localDataSource.saveTokenData(
                    refreshToken = entity.refreshToken ?: "",
                    accessToken = entity.accessToken ?: "",
                    userId = entity.userId,
                    time = System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
                )
                ApiResult.Success(entity.asExternalModel2())
            }

            is ApiResult.Error -> {
                ApiResult.Error(errorMessage = result.errorMessage, code = result.code)
            }
        }
    }
}