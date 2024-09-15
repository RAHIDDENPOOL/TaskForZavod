package ru.zavodteplictesttask.ui.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.zavodteplictesttask.R
import ru.zavodteplictesttask.data.model.AvatarParam
import ru.zavodteplictesttask.data.model.Profile
import ru.zavodteplictesttask.data.model.UserParam
import ru.zavodteplictesttask.di.BASE_URL
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.extensions.getFileName
import ru.zavodteplictesttask.extensions.getZodiacSign
import ru.zavodteplictesttask.extensions.showToast
import ru.zavodteplictesttask.ui.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UserViewModel) {
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.fetchUser()
    }

    var selectedDate by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var vk by rememberSaveable { mutableStateOf("") }
    var instagram by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var avatar by rememberSaveable { mutableStateOf("") }
    var userId by rememberSaveable { mutableStateOf(0) }
    var selectedAvatarUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val profileState by viewModel.profileState.observeAsState()
    val updateProfileState by viewModel.updateProfileState.observeAsState()

    val calendar = Calendar.getInstance()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            selectedAvatarUri = selectedImageUri
            selectedAvatarUri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                imageBytes?.let { byteArray ->
                    val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    val filename = context.contentResolver.getFileName(it) ?: "unknown_filename"

                    viewModel.avatarParam = AvatarParam(
                        filename = filename,
                        base64 = base64Image
                    )
                    selectedAvatarUri = it
                }
            }
        }
    }

    fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(selectedCalendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    val scrollState = rememberScrollState()

    var isLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Image(
            painter = if (selectedAvatarUri != null) {
                rememberAsyncImagePainter(selectedAvatarUri)
            } else if (avatar.isNotBlank()) {
                rememberAsyncImagePainter("$BASE_URL$avatar")
            } else {
                painterResource(id = R.drawable.ic_person)
            },
            contentDescription = stringResource(id = R.string.content_description_user_avatar),
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray.copy(alpha = 0.6f), CircleShape)
                .clickable {
                    openImagePicker()
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone number
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = { Text(stringResource(id = R.string.phone_number)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // USERNAME
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = { Text(stringResource(id = R.string.username)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // City
        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
            },
            label = { Text(stringResource(id = R.string.city)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Date of Birth with DatePickerDialog
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {
                selectedDate = it
            },
            label = { Text(stringResource(id = R.string.birthdate)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    datePickerDialog.show()
                },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(id = R.string.content_description_select_date)
                    )
                }
            }
        )

        // Zodiac Sign
        OutlinedTextField(
            value = if (selectedDate.isBlank()) "" else selectedDate.getZodiacSign(),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.zodiac_sign)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // VK
        OutlinedTextField(
            value = vk,
            onValueChange = {
                vk = it
            },
            label = { Text(stringResource(id = R.string.vk)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // INSTAGRAM
        OutlinedTextField(
            value = instagram,
            onValueChange = {
                instagram = it
            },
            label = { Text(stringResource(id = R.string.instagram)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val avatarParam = viewModel.avatarParam
                viewModel.userParam = UserParam().copy(
                    name = name,
                    username = username,
                    birthday = selectedDate.ifBlank { null },
                    city = city.ifBlank { null },
                    vk = vk.ifBlank { null },
                    instagram = instagram.ifBlank { null },
                    phone = phone,
                    avatars = AvatarParam(
                        filename = avatarParam.filename,
                        base64 = avatarParam.base64
                    ),
                    avatar = avatarParam.filename,
                    userId = userId
                )
                viewModel.updateUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = stringResource(id = R.string.save),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        LaunchedEffect(profileState) {
            when (profileState) {
                is State.Loading -> {}
                is State.Success -> {
                    val profile = (profileState as? State.Success<Profile>)?.data

                    profile?.let {
                        if (city.isEmpty()) city = it.city ?: ""
                        if (phone.isEmpty()) phone = it.phone ?: ""
                        if (name.isEmpty()) name = it.name ?: ""
                        if (username.isEmpty()) username = it.username ?: ""
                        if (vk.isEmpty()) vk = it.vk ?: ""
                        if (instagram.isEmpty()) instagram = it.instagram ?: ""
                        if (selectedDate.isEmpty()) selectedDate = it.birthday ?: ""
                        if (avatar.isEmpty()) avatar = it.avatars?.avatar ?: ""
                        if (userId == 0) userId = it.id
                    }
                }

                is State.Error -> context.showToast((profileState as State.Error).message)
                else -> {}
            }
        }

        LaunchedEffect(updateProfileState) {
            when (updateProfileState) {
                is State.Loading -> {
                    isLoading = true
                }

                is State.Success -> {
                    isLoading = false
                    context.showToast(context.getString(R.string.profile_saved))
                }

                is State.Error -> {
                    isLoading = false
                    context.showToast((updateProfileState as State.Error).message)
                }

                else -> {}
            }
        }
    }
}

