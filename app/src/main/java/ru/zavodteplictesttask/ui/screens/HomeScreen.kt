package ru.zavodteplictesttask.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.zavodteplictesttask.ui.bottomnavbar.BottomNavBar
import ru.zavodteplictesttask.ui.routes.HomeGraph
import ru.zavodteplictesttask.ui.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) {
        HomeGraph(navController = navController, userViewModel = userViewModel, it)
    }
}