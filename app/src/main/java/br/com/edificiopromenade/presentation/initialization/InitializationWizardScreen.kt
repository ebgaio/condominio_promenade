package br.com.edificiopromenade.presentation.initialization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun InitializationWizardScreen(
    onCondominio: () -> Unit,
    onApartamentos: () -> Unit,
    onMoradores: () -> Unit,
    onHome: () -> Unit,
    viewModel: InitializationViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    if (state.loading)
        return

    LaunchedEffect(state.step) {

        when (state.step) {
            InitializationStep.CONDOMINIO -> onCondominio()
            InitializationStep.APARTAMENTOS -> onApartamentos()
            InitializationStep.MORADORES -> onMoradores()
            InitializationStep.HOME -> onHome()
        }
    }
}