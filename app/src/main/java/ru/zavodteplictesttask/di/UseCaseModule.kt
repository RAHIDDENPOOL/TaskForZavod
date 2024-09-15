package ru.zavodteplictesttask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zavodteplictesttask.domain.repository.AuthRepository
import ru.zavodteplictesttask.domain.repository.UserRepository
import ru.zavodteplictesttask.domain.usecases.CheckAuthCodeUseCase
import ru.zavodteplictesttask.domain.usecases.CheckAuthCodeUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.ClearTokensUseCase
import ru.zavodteplictesttask.domain.usecases.ClearTokensUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.FetchTokensUseCase
import ru.zavodteplictesttask.domain.usecases.FetchTokensUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.FetchUserUseCase
import ru.zavodteplictesttask.domain.usecases.FetchUserUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.RegisterUseCase
import ru.zavodteplictesttask.domain.usecases.RegisterUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.SendAuthCodeUseCase
import ru.zavodteplictesttask.domain.usecases.SendAuthCodeUseCaseImpl
import ru.zavodteplictesttask.domain.usecases.UpdateUserUseCase
import ru.zavodteplictesttask.domain.usecases.UpdateUserUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSendAuthCodeUseCase(
        repository: AuthRepository
    ): SendAuthCodeUseCase = SendAuthCodeUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideCheckAuthCodeUseCase(
        repository: AuthRepository
    ): CheckAuthCodeUseCase = CheckAuthCodeUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideRegisterUseCase(
        repository: AuthRepository
    ): RegisterUseCase = RegisterUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideFetchUserUseCase(
        repository: UserRepository
    ): FetchUserUseCase = FetchUserUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideFetchTokensUseCase(
        repository: UserRepository
    ): FetchTokensUseCase = FetchTokensUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideClearTokensUseCase(
        repository: UserRepository
    ): ClearTokensUseCase = ClearTokensUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideUpdateUserUseCase(
        repository: UserRepository
    ): UpdateUserUseCase = UpdateUserUseCaseImpl(repository)
}