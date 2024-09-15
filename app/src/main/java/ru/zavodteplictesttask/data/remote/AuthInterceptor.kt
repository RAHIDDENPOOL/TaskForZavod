package ru.zavodteplictesttask.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.zavodteplictesttask.data.local.LocalDataSource.Companion.ACCESS_TOKEN_KEY
import ru.zavodteplictesttask.data.local.LocalDataSource.Companion.ACCESS_TOKEN_SAVED_TIME_KEY
import ru.zavodteplictesttask.data.local.LocalDataSource.Companion.REFRESH_TOKEN_KEY
import ru.zavodteplictesttask.data.local.LocalDataSource.Companion.USER_ID_KEY
import ru.zavodteplictesttask.data.local.dataStore
import ru.zavodteplictesttask.data.remote.api.TokenApi
import ru.zavodteplictesttask.data.remote.request.RefreshTokenRequest

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")

        val pairAccessTokenAndExpirationTime = runBlocking {
            val accessToken = context.dataStore.data.map { prefs ->
                prefs[ACCESS_TOKEN_KEY]
            }.first()

            val accessTokenExpirationTime = context.dataStore.data.map { prefs ->
                prefs[ACCESS_TOKEN_SAVED_TIME_KEY]
            }.first()

            Pair(accessToken, accessTokenExpirationTime)
        }


        val accessToken = pairAccessTokenAndExpirationTime.first
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(request.build())
        } else {
            val accessTokenExpirationTime = pairAccessTokenAndExpirationTime.second
            if (accessTokenExpirationTime != null && System.currentTimeMillis() >= accessTokenExpirationTime) {
                val tokenApi = ServiceGenerator.generate(TokenApi::class.java)
                runBlocking {
                    val refreshToken = context.dataStore.data.map { prefs ->
                        prefs[REFRESH_TOKEN_KEY]
                    }.first() ?: ""

                    val refreshTokenDto =
                        tokenApi.refreshTokenAsync(RefreshTokenRequest(refreshToken))
                    request.addHeader("Authorization", "Bearer ${refreshTokenDto.accessToken}")

                    context.dataStore.edit { preferences ->
                        preferences[REFRESH_TOKEN_KEY] = refreshTokenDto.refreshToken
                        preferences[ACCESS_TOKEN_KEY] = refreshTokenDto.accessToken
                        preferences[USER_ID_KEY] = refreshTokenDto.userId

                        preferences[ACCESS_TOKEN_SAVED_TIME_KEY] =
                            System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
                    }
                }
            } else {
                request.addHeader("Authorization", "Bearer $accessToken")
            }
        }

        return chain.proceed(request.build())
    }
}