package ru.zavodteplictesttask.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.zavodteplictesttask.domain.model.State
import ru.zavodteplictesttask.ui.bottomnavbar.BottomNavItem
import ru.zavodteplictesttask.ui.screens.ChatMessagesScreen
import ru.zavodteplictesttask.ui.screens.ChatsScreen
import ru.zavodteplictesttask.ui.screens.HomeScreen
import ru.zavodteplictesttask.ui.screens.PhoneNumberScreen
import ru.zavodteplictesttask.ui.screens.ProfileScreen
import ru.zavodteplictesttask.ui.screens.RegisterScreen
import ru.zavodteplictesttask.ui.screens.SmsCodeScreen
import ru.zavodteplictesttask.ui.viewmodel.AuthViewModel
import ru.zavodteplictesttask.ui.viewmodel.UserTokenState
import ru.zavodteplictesttask.ui.viewmodel.UserViewModel

@Composable
fun RootNavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val userTokenState by userViewModel.userTokenState.observeAsState()

    LaunchedEffect(LocalContext.current) {
        userViewModel.getUserToken()
    }

    when (userTokenState) {
        is State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is State.Success -> {
            val userTokenStateResult = (userTokenState as? State.Success<UserTokenState>)?.data
            val initialDestination =
                if (userTokenStateResult?.userToken?.accessToken.isNullOrBlank()) Graph.AUTH else Graph.HOME
            NavHost(
                navController = navController,
                route = Graph.ROOT,
                startDestination = initialDestination
            ) {
                authNavGraph(navController, authViewModel)
                composable(route = Graph.HOME) {
                    HomeScreen(userViewModel = userViewModel)
                }
            }
        }

        is State.Error -> {
            Text((userTokenState as State.Error).message ?: "")
        }

        else -> {}
    }
}

@Composable
fun HomeGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    innerPaddings: PaddingValues
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = ScreenRoutes.ChatsScreen.route,
        modifier = Modifier.padding(innerPaddings)
    ) {
        composable(BottomNavItem.Chats.route) {
            ChatsScreen(
                onChatSelected = {
                    navController.navigate(ScreenRoutes.ChatMessagesScreen.route)
                }
            )
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen(userViewModel)
        }

        composable(route = ScreenRoutes.ChatMessagesScreen.route) {
            ChatMessagesScreen(onBackPressed = {
                navController.popBackStack()
            })
        }
    }
}


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = ScreenRoutes.PhoneNumberScreen.route,
        route = Graph.AUTH
    ) {
        composable(route = ScreenRoutes.PhoneNumberScreen.route) {
            PhoneNumberScreen(
                onPhoneNumberSubmitted = { phone ->
                    navController.navigate("sms_code/$phone")
                },
                viewModel = authViewModel
            )
        }

        composable(
            route = ScreenRoutes.SmsCodeScreen.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            SmsCodeScreen(
                viewModel = authViewModel,
                phone = phone,
                onProfileNavigate = {
                    navController.navigate(ScreenRoutes.ProfileScreen.route)
                },
                onRegistrationRequired = { aPhone ->
                    navController.navigate("register/$aPhone")
                }
            )
        }

        composable(
            route = ScreenRoutes.RegisterScreen.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            RegisterScreen(
                viewModel = authViewModel,
                phoneNumber = phone,
                onRegister = {
                    navController.navigate(Graph.HOME)
                }
            )
        }
    }
}



