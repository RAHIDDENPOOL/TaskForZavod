package ru.zavodteplictesttask.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.zavodteplictesttask.R
import ru.zavodteplictesttask.data.model.UserToken
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.extensions.showToast
import ru.zavodteplictesttask.ui.viewmodel.AuthViewModel

@Composable
fun SmsCodeScreen(
    phone: String,
    onProfileNavigate: () -> Unit,
    onRegistrationRequired: (String) -> Unit,
    viewModel: AuthViewModel,
) {
    val context = LocalContext.current

    val checkAuthCodeState by viewModel.checkAuthCodeState.observeAsState()

    var smsCode by remember { mutableStateOf("") }
    var isLoading: Boolean by remember { mutableStateOf(false) }
    var errorMessage: String? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.enter_sms_code),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = smsCode,
            onValueChange = { if (it.length <= 6) smsCode = it },
            placeholder = { Text(text = stringResource(id = R.string.sms_code)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (smsCode.length == 6) {
                    viewModel.checkAuthCode(phone, smsCode)
                } else {
                    errorMessage = context.getString(R.string.error_sms_code_length)
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = stringResource(id = R.string.submit),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LaunchedEffect(checkAuthCodeState) {
            when (checkAuthCodeState) {
                is State.Loading -> {
                    isLoading = true
                }

                is State.Success -> {
                    isLoading = false
                    val userToken = (checkAuthCodeState as State.Success<UserToken>).data
                    if (userToken?.isUserExists == true) {
                        onProfileNavigate()
                    } else {
                        onRegistrationRequired(phone)
                    }
                }

                is State.Error -> {
                    isLoading = false
                    context.showToast((checkAuthCodeState as State.Error).message)
                }

                else -> {}
            }
        }
    }
}
