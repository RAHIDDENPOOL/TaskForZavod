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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.togitech.ccp.component.TogiCountryCodePicker
import ru.zavodteplictesttask.R
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.extensions.showToast
import ru.zavodteplictesttask.ui.viewmodel.AuthViewModel

@Composable
fun PhoneNumberScreen(
    onPhoneNumberSubmitted: (String) -> Unit,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current

    val sendAuthCodeState by viewModel.sendAuthCodeState.observeAsState()

    var phoneNumber: String by remember { mutableStateOf("") }
    var fullPhoneNumber: String by remember { mutableStateOf("") }
    var isNumberValid: Boolean by remember { mutableStateOf(false) }
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
            stringResource(id = R.string.enter_phone_number),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        TogiCountryCodePicker(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            onValueChange = { (code, phone), isValid ->
                phoneNumber = phone
                fullPhoneNumber = code + phone
                isNumberValid = isValid
            },
            label = { Text(stringResource(id = R.string.phone_number)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (isNumberValid) {
                    viewModel.sendAuthCode(fullPhoneNumber) // Call the ViewModel function
                } else {
                    errorMessage = context.getString(R.string.error_phone_number)
                }
            },
            enabled = !isLoading, // Disable the button while loading
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
                    text = stringResource(id = R.string.send_code),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium
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

        when (sendAuthCodeState) {
            is State.Loading -> {
                isLoading = true
            }

            is State.Success -> {
                isLoading = false
                onPhoneNumberSubmitted(fullPhoneNumber)
            }

            is State.Error -> {
                isLoading = false
                context.showToast((sendAuthCodeState as State.Error).message)
            }

            else -> {}
        }
    }

}