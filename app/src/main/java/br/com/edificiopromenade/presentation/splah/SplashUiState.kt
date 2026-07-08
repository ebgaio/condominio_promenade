package br.com.edificiopromenade.presentation.splah

import br.com.edificiopromenade.domain.model.AppInitializationResult

data class SplashUiState(

    val loading: Boolean = true,

    val initializationResult: AppInitializationResult? = null
)