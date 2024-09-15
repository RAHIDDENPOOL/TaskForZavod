package ru.zavodteplictesttask.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.zavodteplictesttask.data.local.LocalDataSource
import ru.zavodteplictesttask.data.remote.AuthRemoteDataSource
import ru.zavodteplictesttask.data.remote.UserRemoteDataSource
import ru.zavodteplictesttask.data.remote.api.AuthApi
import ru.zavodteplictesttask.data.remote.api.UserApi
import ru.zavodteplictesttask.data.repository.AuthRepositoryImpl
import ru.zavodteplictesttask.data.repository.UserRepositoryImpl
import ru.zavodteplictesttask.domain.repository.AuthRepository
import ru.zavodteplictesttask.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(
        api: AuthApi,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) = AuthRemoteDataSource(api, coroutineDispatcher)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        api: UserApi,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) = UserRemoteDataSource(api, coroutineDispatcher)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @ApplicationContext appContext: Context,
    ) = LocalDataSource(appContext)

    @Singleton
    @Provides
    fun provideAuthRepository(
        localDataSource: LocalDataSource,
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository = AuthRepositoryImpl(localDataSource, authRemoteDataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        localDataSource: LocalDataSource,
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository = UserRepositoryImpl(localDataSource, userRemoteDataSource)

}