package br.com.edificiopromenade.presentation.splah

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import br.com.edificiopromenade.domain.model.AppInitializationResult
import br.com.edificiopromenade.presentation.navigation.AppDestinations

@Composable
fun SplashScreen(
    onReady: () -> Unit,
    onNeedCondominio: () -> Unit,
    onNeedApartamento: () -> Unit,
    onNeedMorador: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.initializationResult) {

        when (state.initializationResult) {

            AppInitializationResult.Ready -> onReady()
            AppInitializationResult.NeedCondominio -> onNeedCondominio()
            AppInitializationResult.NeedApartamento -> onNeedApartamento()
            AppInitializationResult.NeedMorador -> onNeedMorador()
            null -> {}
        }
    }

//    val navController = rememberNavController()

//    LaunchedEffect(state.initializationResult) {
//
//        navController.navigate(
//    AppDestinations.INITIALIZATION
//        ) {
//            popUpTo(AppDestinations.SPLASH) {
//                inclusive = true
//            }
//        }
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}