package br.com.edificiopromenade.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.edificiopromenade.presentation.apartment.ApartamentoScreen
import br.com.edificiopromenade.presentation.apartment.detail.ApartamentoDetalheScreen
import br.com.edificiopromenade.presentation.condominio.CondominioScreen
import br.com.edificiopromenade.presentation.home.HomeScreen
import br.com.edificiopromenade.presentation.moradores.MoradoresScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

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
                },
                onCondominioClick = {

                    navController.navigate(
                        AppDestinations.CONDOMINIO
                    )
                },
                onApartamentosClick = {

                    navController.navigate(
                        AppDestinations.APARTAMENTOS
                    )
                }
            )
        }

        composable(
            AppDestinations.MORADORES
        ) {
            MoradoresScreen()
        }

        composable(
            AppDestinations.CONDOMINIO
        ) {
            CondominioScreen()
        }

        composable(
            AppDestinations.APARTAMENTOS
        ) {
            ApartamentoScreen(
                navController = navController
            )
        }

        composable(
            route = AppDestinations.APARTAMENTO_DETALHE
        ) { backStackEntry ->

            val id = backStackEntry
                    .arguments
                    ?.getString("id")
                    ?.toLongOrNull()
                    ?: 0L

            ApartamentoDetalheScreen(
                apartamentoId = id,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }
    }
}