package ru.zavodteplictesttask.ui.routes

sealed class ScreenRoutes(val route: String) {
    data object PhoneNumberScreen : ScreenRoutes("phone_number")
    data object SmsCodeScreen : ScreenRoutes("sms_code/{phone}")
    data object RegisterScreen : ScreenRoutes("register/{phone}")

    data object ProfileScreen : ScreenRoutes("profile")
    data object ChatsScreen : ScreenRoutes("chats")
    data object ChatMessagesScreen : ScreenRoutes("chat_messages")
}