package br.com.edificiopromenade.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.edificiopromenade.presentation.apartment.ApartamentoScreen
import br.com.edificiopromenade.presentation.apartment.detail.ApartamentoDetalheScreen
import br.com.edificiopromenade.presentation.condominio.CondominioScreen
import br.com.edificiopromenade.presentation.demonstrativo.DemonstrativosScreen
import br.com.edificiopromenade.presentation.despesa.DespesaScreen
import br.com.edificiopromenade.presentation.despesaitem.DespesaItemScreen
import br.com.edificiopromenade.presentation.despesatemporaria.DespesaTemporariaScreen
import br.com.edificiopromenade.presentation.fechamento.NovoFechamentoScreen
import br.com.edificiopromenade.presentation.history.HistoryScreen
import br.com.edificiopromenade.presentation.home.HomeScreen
import br.com.edificiopromenade.presentation.initialization.InitializationFlowScreen
import br.com.edificiopromenade.presentation.moradores.MoradoresScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.INITIALIZATION,
        modifier = modifier
    ) {

        /*
         * ============================================================
         * FLUXO DE INSTALAÇÃO INICIAL
         * ============================================================
         */
        composable(
            route = AppDestinations.INITIALIZATION
        ) {

            InitializationFlowScreen(

                onCondominio = {
                    navController.navigate(
                        AppDestinations.condominioRoute(
                            modoInicializacao = true
                        )
                    ) {
                        popUpTo(
                            AppDestinations.INITIALIZATION
                        ) {
                            inclusive = true
                        }
                    }
                },

                onApartamentos = {
                    navController.navigate(
                        AppDestinations.apartamentosRoute(
                            modoInicializacao = true
                        )
                    ) {
                        popUpTo(
                            AppDestinations.INITIALIZATION
                        ) {
                            inclusive = true
                        }
                    }
                },

                onMoradores = {
                    navController.navigate(
                        AppDestinations.moradoresRoute(
                            modoInicializacao = true
                        )
                    ) {
                        popUpTo(
                            AppDestinations.INITIALIZATION
                        ) {
                            inclusive = true
                        }
                    }
                },

                onHome = {
                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.INITIALIZATION
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        /*
         * ============================================================
         * HOME
         * ============================================================
         *
         * Todas as telas abertas diretamente pela Home entram
         * em modo manutenção.
         *
         * modoInicializacao = false
         * ============================================================
         */
        composable(
            route = AppDestinations.HOME
        ) {

            HomeScreen(

                onMoradoresClick = {
                    navController.navigate(
                        AppDestinations.moradoresRoute(
                            modoInicializacao = false
                        )
                    )
                },

                onCondominioClick = {
                    navController.navigate(
                        AppDestinations.condominioRoute(
                            modoInicializacao = false
                        )
                    )
                },

                onApartamentosClick = {
                    navController.navigate(
                        AppDestinations.apartamentosRoute(
                            modoInicializacao = false
                        )
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

        /*
         * ============================================================
         * HISTÓRICO
         * ============================================================
         */
        composable(
            route = AppDestinations.HISTORICO
        ) {

            HistoryScreen(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        /*
         * ============================================================
         * CONDOMÍNIO
         * ============================================================
         *
         * A rota pode ser aberta:
         *
         * 1. Pela instalação inicial
         * 2. Pela Home
         *
         * O comportamento é definido por modoInicializacao.
         * ============================================================
         */
        composable(
            route = AppDestinations.CONDOMINIO,
            arguments = listOf(

                navArgument(
                    "modoInicializacao"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->

            val modoInicializacao = backStackEntry
                    .arguments
                    ?.getBoolean(
                        "modoInicializacao"
                    )
                    ?: false

            CondominioScreen(

                modoInicializacao = modoInicializacao,
                onProximo = {
                    navController.navigate(
                        AppDestinations.apartamentosRoute(
                            modoInicializacao = modoInicializacao
                        )
                    )
                },

                onSair = {

                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.HOME
                        ) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * ============================================================
         * APARTAMENTOS
         * ============================================================
         */
        composable(
            route = AppDestinations.APARTAMENTOS,
            arguments = listOf(

                navArgument(
                    "modoInicializacao"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->

            val modoInicializacao = backStackEntry
                    .arguments
                    ?.getBoolean(
                        "modoInicializacao"
                    )
                    ?: false

            ApartamentoScreen(

                navController = navController,
                modoInicializacao = modoInicializacao,
                onAnterior = {
                    navController.popBackStack()
                },
                onProximo = {
                    navController.navigate(
                        AppDestinations.moradoresRoute(
                            modoInicializacao = modoInicializacao
                        )
                    )
                },

                onSair = {

                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.HOME
                        ) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * ============================================================
         * MORADORES
         * ============================================================
         *
         * Na instalação inicial:
         *
         * Moradores → Concluído → Home
         *
         * Em manutenção:
         *
         * A navegação será ajustada na Etapa 1.7.
         * ============================================================
         */
        composable(
            route = AppDestinations.MORADORES,
            arguments = listOf(

                navArgument(
                    "modoInicializacao"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->

            val modoInicializacao = backStackEntry
                    .arguments
                    ?.getBoolean(
                        "modoInicializacao"
                    )
                    ?: false

            MoradoresScreen(

                modoInicializacao = modoInicializacao,
                onAnterior = {
                    navController.popBackStack()
                },

                onProximo = {
                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.HOME
                        ) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                },

                onSair = {

                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.HOME
                        ) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * ============================================================
         * DETALHE DO APARTAMENTO
         * ============================================================
         */
        composable(
            route = AppDestinations.APARTAMENTO_DETALHE
        ) { backStackEntry ->

            val id =
                backStackEntry
                    .arguments
                    ?.getString(
                        "id"
                    )
                    ?.toLongOrNull()
                    ?: 0L

            ApartamentoDetalheScreen(

                apartamentoId = id,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        /*
         * ============================================================
         * NOVO FECHAMENTO
         * ============================================================
         */
        composable(
            route = AppDestinations.NOVO_FECHAMENTO
        ) {

            NovoFechamentoScreen(
                onAbrirDespesas = { fechamentoId ->
                    navController.navigate(
                        AppDestinations.despesasRoute(
                            fechamentoId
                        )
                    )
                },

                onSair = {
                    navController.navigate(
                        AppDestinations.HOME
                    ) {
                        popUpTo(
                            AppDestinations.HOME
                        ) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * ============================================================
         * DESPESAS
         * ============================================================
         */
        composable(
            route = AppDestinations.DESPESAS
        ) { backStackEntry ->

            val fechamentoId = backStackEntry
                    .arguments
                    ?.getString(
                        "fechamentoId"
                    )
                    ?.toLongOrNull()
                    ?: 0L

            DespesaScreen(
                fechamentoId = fechamentoId,
                onVoltar = {
                    navController.popBackStack()
                },
                onAbrirDemonstrativos = { id ->
                    navController.navigate(
                        AppDestinations
                            .demonstrativosRoute(id)
                    )
                },
                onAbrirItensDespesa = { despesaId ->
                    navController.navigate(
                        AppDestinations.despesaItemRoute(
                            despesaId
                        )
                    )
                },
                onAdicionarDespesaAvulsa = { id ->
                    navController.navigate(
                        AppDestinations.despesaTemporariaRoute(
                            id
                        )
                    )
                }
            )
        }

        /*
         * ============================================================
         * ITENS DA DESPESA
         * ============================================================
         */
        composable(
            route = AppDestinations.DESPESA_ITEM,
            arguments = listOf(
                navArgument(
                    "despesaId"
                ) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val despesaId =
                backStackEntry
                    .arguments!!
                    .getLong(
                        "despesaId"
                    )

            DespesaItemScreen(
                despesaId = despesaId,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        /*
         * ============================================================
         * DEMONSTRATIVOS
         * ============================================================
         */
        composable(
            route = AppDestinations.DEMONSTRATIVOS
        ) { backStackEntry ->

            val fechamentoId = backStackEntry
                    .arguments
                    ?.getString(
                        "fechamentoId"
                    )
                    ?.toLongOrNull()
                    ?: 0L

            DemonstrativosScreen(

                fechamentoId = fechamentoId,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        /*
         * ============================================================
         * DESPESA TEMPORARIA
         * ============================================================
         */
        composable(
            route = AppDestinations.DESPESA_TEMPORARIA,
            arguments = listOf(
                navArgument("fechamentoId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val fechamentoId = backStackEntry.arguments
                    ?.getLong("fechamentoId")
                    ?: 0L

            DespesaTemporariaScreen(
                fechamentoId = fechamentoId,
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }
    }
}
