package ru.zavodteplictesttask.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.zavodteplictesttask.data.model.PhoneSuccess
import ru.zavodteplictesttask.data.model.Register
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.data.remote.api.ApiResult
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.domain.usecases.CheckAuthCodeUseCase
import ru.zavodteplictesttask.domain.usecases.PhoneParam
import ru.zavodteplictesttask.domain.usecases.PhoneWithCodeParam
import ru.zavodteplictesttask.domain.usecases.RegisterParam
import ru.zavodteplictesttask.domain.usecases.RegisterUseCase
import ru.zavodteplictesttask.domain.usecases.SendAuthCodeUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _sendAuthCodeState = MutableLiveData<State<PhoneSuccess>>()
    val sendAuthCodeState: LiveData<State<PhoneSuccess>> get() = _sendAuthCodeState

    private val _checkAuthCodeState = MutableLiveData<State<UserToken>>()
    val checkAuthCodeState: LiveData<State<UserToken>> get() = _checkAuthCodeState

    private val _registerState = MutableLiveData<State<Register>>()
    val registerState: LiveData<State<Register>> get() = _registerState

    var registerParam: RegisterParam = RegisterParam()

    fun sendAuthCode(phone: String) {
        viewModelScope.launch {
            sendAuthCodeUseCase(PhoneParam(phone))
                .onStart {
                    _sendAuthCodeState.value = State.Loading
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _sendAuthCodeState.value = State.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _sendAuthCodeState.value = State.Error(message = result.errorMessage)
                        }
                    }
                }
        }
    }

    fun checkAuthCode(phone: String, code: String) {
        viewModelScope.launch {
            checkAuthCodeUseCase(PhoneWithCodeParam(phone = phone, code = code))
                .onStart {
                    _checkAuthCodeState.value = State.Loading
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _checkAuthCodeState.value = State.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _checkAuthCodeState.value = State.Error(message = result.errorMessage)
                        }
                    }
                }
        }
    }

    fun register() {
        viewModelScope.launch {
            registerUseCase(registerParam)
                .onStart {
                    _registerState.value = State.Loading
                }.collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _registerState.value = State.Success(result.data)
                        }

                        is ApiResult.Error -> {
                            _registerState.value = State.Error(message = result.errorMessage)
                        }
                    }
                }
        }
    }
}