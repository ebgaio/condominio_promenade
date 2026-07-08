package br.com.edificiopromenade.presentation.initialization

data class InitializationUiState(

    val loading: Boolean = true,

    val step: InitializationStep = InitializationStep.CONDOMINIO
)