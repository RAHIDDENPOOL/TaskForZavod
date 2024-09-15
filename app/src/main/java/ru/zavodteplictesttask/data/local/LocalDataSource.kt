package ru.zavodteplictesttask.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import ru.zavodteplictesttask.data.local.entity.TokenEntity
import ru.zavodteplictesttask.data.local.entity.UserAvatarEntity
import ru.zavodteplictesttask.data.local.entity.UserEntity

class LocalDataSource(private val context: Context) {
    companion object {
        val PHONE_KEY = stringPreferencesKey(PHONE)
        val NAME_KEY = stringPreferencesKey(NAME)
        val USERNAME_KEY = stringPreferencesKey(USERNAME)
        val CITY_KEY = stringPreferencesKey(CITY)
        val BIRTHDAY_KEY = stringPreferencesKey(BIRTHDAY)
        val USER_ID_KEY = intPreferencesKey(USER_ID)
        val AVATAR_KEY = stringPreferencesKey(AVATAR)
        val AVATAR_OTHER_KEY = stringPreferencesKey(AVATAR_OTHER)
        val VK_KEY = stringPreferencesKey(VK)
        val INSTAGRAM_KEY = stringPreferencesKey(INSTAGRAM)
        val ACCESS_TOKEN_SAVED_TIME_KEY = longPreferencesKey(ACCESS_TOKEN_SAVED_TIME)

        val REFRESH_TOKEN_KEY = stringPreferencesKey(REFRESH_TOKEN)
        val ACCESS_TOKEN_KEY = stringPreferencesKey(ACCESS_TOKEN)
    }

    suspend fun saveAllUserData(userEntity: UserEntity) {
        context.dataStore.edit { preferences ->
            with(userEntity) {
                preferences[PHONE_KEY] = phone
                preferences[NAME_KEY] = name ?: ""
                preferences[USERNAME_KEY] = username
                preferences[CITY_KEY] = city ?: ""
                preferences[BIRTHDAY_KEY] = birthday ?: ""
                preferences[USER_ID_KEY] = id
                preferences[AVATAR_KEY] = avatar ?: ""
                preferences[AVATAR_OTHER_KEY] = avatars?.avatar ?: ""
                preferences[VK_KEY] = vk ?: ""
                preferences[INSTAGRAM_KEY] = instagram ?: ""
            }
        }
    }


    suspend fun saveTokenData(
        refreshToken: String,
        accessToken: String,
        userId: Int,
        time: Long
    ) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[ACCESS_TOKEN_SAVED_TIME_KEY] = time
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun getSavedUser(): UserEntity {
        val preferences = context.dataStore.data.first()
        val phone = preferences[PHONE_KEY] ?: ""
        val name = preferences[NAME_KEY]
        val username = preferences[USERNAME_KEY] ?: ""
        val city = preferences[CITY_KEY]
        val birthday = preferences[BIRTHDAY_KEY]
        val userId = preferences[USER_ID_KEY] ?: 0
        val avatar = preferences[AVATAR_KEY]
        val avatarOther = preferences[AVATAR_OTHER_KEY]
        val vk = preferences[VK_KEY]
        val instagram = preferences[INSTAGRAM_KEY]
        return UserEntity(
            name = name,
            phone = phone,
            username = username,
            city = city,
            birthday = birthday,
            id = userId,
            avatar = avatar,
            vk = vk,
            instagram = instagram,
            avatars = UserAvatarEntity(avatar = avatarOther)
        )
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = ""
            preferences[ACCESS_TOKEN_KEY] = ""
            preferences[USER_ID_KEY] = 0
            preferences[ACCESS_TOKEN_SAVED_TIME_KEY] = 0
        }
    }

    suspend fun getSavedTokens(): TokenEntity {
        val prefs = context.dataStore.data.first()
        val refreshToken = prefs[REFRESH_TOKEN_KEY] ?: ""
        val accessToken = prefs[ACCESS_TOKEN_KEY] ?: ""
        val userId = prefs[USER_ID_KEY] ?: 0
        val tokenTime = prefs[ACCESS_TOKEN_SAVED_TIME_KEY]
        return TokenEntity(
            refreshToken = refreshToken,
            accessToken = accessToken,
            userId = userId,
            tokenTime = tokenTime
        )
    }

}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private const val PHONE = "phone"
private const val NAME = "name"
private const val USERNAME = "username"
private const val CITY = "city"
private const val BIRTHDAY = "birthday"
private const val USER_ID = "user_id"
private const val AVATAR = "avatar"
private const val AVATAR_OTHER = "avatar_other"
private const val VK = "vk"
private const val INSTAGRAM = "instagram"
private const val ACCESS_TOKEN_SAVED_TIME = "access_token_saved_time"
private const val REFRESH_TOKEN = "refresh_token"
private const val ACCESS_TOKEN = "access_token"