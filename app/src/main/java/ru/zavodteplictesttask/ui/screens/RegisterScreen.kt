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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.zavodteplictesttask.R
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.domain.usecases.RegisterParam
import ru.zavodteplictesttask.extensions.showToast
import ru.zavodteplictesttask.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    phoneNumber: String,
    onRegister: () -> Unit
) {
    val registerState by viewModel.registerState.observeAsState()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isValidUsername by remember { mutableStateOf(true) }
    val errorMessage: String? by remember { mutableStateOf(null) }
    var isLoading: Boolean by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // phone number
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.phone_number)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = stringResource(id = R.string.name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // username
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                isValidUsername = validateUsername(it)
            },
            isError = !isValidUsername,
            label = { Text(text = stringResource(id = R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Ascii
            ),
        )

        if (!isValidUsername) {
            Text(
                text = stringResource(id = R.string.error_username),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Button(
            onClick = {
                if (isValidUsername) {
                    viewModel.registerParam = RegisterParam(
                        username = username,
                        name = name,
                        phone = phoneNumber
                    )
                    viewModel.register()
                } else {
                    context.showToast(context.getString(R.string.error_register))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = stringResource(id = R.string.register),
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

        LaunchedEffect(registerState) {
            when (registerState) {
                is State.Loading -> {
                    isLoading = true
                }

                is State.Success -> {
                    onRegister()
                    isLoading = false
                }

                is State.Error -> {
                    isLoading = false
                    context.showToast((registerState as State.Error).message)
                }

                else -> {}
            }
        }
    }
}

fun validateUsername(username: String): Boolean {
    // Regular expression to match the allowed characters
    val usernameRegex = "^[A-Za-z0-9-_]+$".toRegex()
    return username.matches(usernameRegex)
}
