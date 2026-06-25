package br.com.edificiopromenade.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.edificiopromenade.presentation.apartment.ApartamentoScreen
import br.com.edificiopromenade.presentation.apartment.detail.ApartamentoDetalheScreen
import br.com.edificiopromenade.presentation.condominio.CondominioScreen
import br.com.edificiopromenade.presentation.demonstrativo.DemonstrativosScreen
import br.com.edificiopromenade.presentation.despesa.DespesaScreen
import br.com.edificiopromenade.presentation.fechamento.NovoFechamentoScreen
import br.com.edificiopromenade.presentation.history.HistoryScreen
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
                },
                onNovoFechamentoClick = {
                    navController.navigate(
                        AppDestinations.NOVO_FECHAMENTO
                    )
                },
                onHistoricoClick = {
                    navController.navigate(
                        AppDestinations.HISTORICO
                    )
                }
            )
        }

        composable(
            AppDestinations.HISTORICO
        ) {
            HistoryScreen(
                onVoltar = {
                    navController.popBackStack()
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

        composable(
            AppDestinations.NOVO_FECHAMENTO
        ) {
            NovoFechamentoScreen(
                navController = navController
            )
        }

        composable(
            route = AppDestinations.DESPESAS
        ) { backStackEntry ->

            val fechamentoId =
                backStackEntry
                    .arguments
                    ?.getString("fechamentoId")
                    ?.toLongOrNull()
                    ?: 0L

            DespesaScreen(
                fechamentoId = fechamentoId,

                onVoltar = {
                    navController.popBackStack()
                },
                onAbrirDemonstrativos = { id ->
                    navController.navigate(
                        AppDestinations.demonstrativosRoute(id)
                    )
                }
            )
        }

        composable(
            route = AppDestinations.DEMONSTRATIVOS
        ) { backStackEntry ->

            val fechamentoId = backStackEntry
                    .arguments
                    ?.getString("fechamentoId")
                    ?.toLongOrNull()
                    ?: 0L

            DemonstrativosScreen(
                fechamentoId = fechamentoId,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }
    }
}