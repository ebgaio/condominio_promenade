package br.com.edificiopromenade.domain.model

sealed interface AppInitializationResult {

    data object Ready : AppInitializationResult

    data object NeedCondominio : AppInitializationResult

    data object NeedApartamento : AppInitializationResult

    data object NeedMorador : AppInitializationResult
}