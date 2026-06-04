package br.com.edificiopromenade.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.edificiopromenade.presentation.home.HomeScreen
import br.com.edificiopromenade.presentation.moradores.MoradoresScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME,
        modifier = modifier
    ) {

        composable(
            AppDestinations.HOME
        ) {

            HomeScreen(
                onMoradoresClick = {

                    navController.navigate(
                        AppDestinations.MORADORES
                    )
                }
            )
        }

        composable(
            AppDestinations.MORADORES
        ) {

            MoradoresScreen()
        }
    }
}