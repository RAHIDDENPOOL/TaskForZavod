package ru.zavodteplictesttask.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.zavodteplictesttask.R

@Composable
fun ChatsScreen(onChatSelected: (String) -> Unit) {
    val context = LocalContext.current

    val chatList = context.resources.getStringArray(R.array.chat_list)
    val colors = listOf(
        Color.Black, Color.Gray, Color.Magenta, Color.Cyan
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(chatList) { chat ->
            val index = chatList.indexOf(chat)
            val color = colors[index % colors.size]
            ChatListItem(
                chat = chat,
                onClick = { onChatSelected(chat) },
                index = index,
                color = color
            )
            Divider()
        }
    }
}

@Composable
fun ChatListItem(chat: String, onClick: () -> Unit, index: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(54.dp)
                .background(color.copy(alpha = 0.5f), shape = CircleShape)
        ) {
            Text(
                text = chat.take(1) + "$index",
                style = MaterialTheme.typography.subtitle1, // Text style
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(text = chat, style = MaterialTheme.typography.subtitle1)
    }
}
