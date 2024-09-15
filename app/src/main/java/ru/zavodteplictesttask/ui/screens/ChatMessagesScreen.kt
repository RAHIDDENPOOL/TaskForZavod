package ru.zavodteplictesttask.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zavodteplictesttask.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMessagesScreen(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val messageArray = context.resources.getStringArray(R.array.message_list)
    val messageList = remember { mutableStateListOf(*messageArray) }

    var newMessage by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.messages),
                    style = TextStyle(fontSize = 18.sp)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back
                        )
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messageList) { message ->
                MessageListItem(message)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            OutlinedTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                label = null,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 56.dp)
            )

            IconButton(
                onClick = {
                    if (newMessage.isNotBlank()) {
                        messageList.add(newMessage)
                        newMessage = ""
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.send)
                )
            }
        }
    }
}


@Composable
fun MessageListItem(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(id = R.string.content_description_message_icon),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = message, style = MaterialTheme.typography.bodySmall)
    }
}
