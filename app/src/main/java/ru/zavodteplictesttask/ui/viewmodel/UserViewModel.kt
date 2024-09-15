package ru.zavodteplictesttask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.zavodteplictesttask.data.model.Avatar
import ru.zavodteplictesttask.data.model.AvatarParam
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.model.UserParam
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.domain.usecases.FetchTokensUseCase
import ru.zavodteplictesttask.domain.usecases.FetchUserUseCase
import ru.zavodteplictesttask.domain.usecases.UpdateUserUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val fetchTokensUseCase: FetchTokensUseCase
) : ViewModel() {

    private val _profileState = MutableLiveData<State<Profile>>()
    val profileState: LiveData<State<Profile>> get() = _profileState

    private val _updateProfileState = MutableLiveData<State<Avatar>>()
    val updateProfileState: LiveData<State<Avatar>> get() = _updateProfileState

    private val _userTokenState = MutableLiveData<State<UserTokenState>>()
    val userTokenState: LiveData<State<UserTokenState>> get() = _userTokenState

    var userParam = UserParam()
    var avatarParam = AvatarParam("", "")

    fun fetchUser() {
        viewModelScope.launch {
            fetchUserUseCase(false)
                .onStart {
                    _profileState.value = State.Loading
                }
                .collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _profileState.value = State.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _profileState.value = State.Error(message = result.errorMessage)
                        }
                    }
                }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            updateUserUseCase(userParam)
                .onStart {
                    _updateProfileState.value = State.Loading
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _updateProfileState.value = State.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _updateProfileState.value = State.Error(
                                message = result.errorMessage
                            )
                        }
                    }
                }
        }
    }

    fun getUserToken() {
        viewModelScope.launch {
            fetchTokensUseCase(Unit)
                .onStart {
                    _userTokenState.value = State.Loading
                }.collectLatest { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _userTokenState.value =
                                State.Success(UserTokenState(userToken = result.data))
                        }

                        is ApiResult.Error -> {
                            _userTokenState.value = State.Error(
                                message = result.errorMessage
                            )
                        }
                    }
                }
        }
    }
}

data class UserTokenState(
    val userToken: UserToken? = null,
    val errorMessage: String? = null
)
