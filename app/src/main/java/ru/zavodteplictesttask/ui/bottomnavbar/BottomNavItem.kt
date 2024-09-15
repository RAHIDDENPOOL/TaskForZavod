package ru.zavodteplictesttask.ui.bottomnavbar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import ru.zavodteplictesttask.R
import ru.zavodteplictesttask.ui.routes.ScreenRoutes

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourseId: Int
) {
    data object Chats : BottomNavItem(
        ScreenRoutes.ChatsScreen.route,
        Icons.Default.ChatBubbleOutline,
        R.string.chats
    )

    data object Profile : BottomNavItem(
        ScreenRoutes.ProfileScreen.route,
        Icons.Default.PersonOutline,
        R.string.profile
    )
}