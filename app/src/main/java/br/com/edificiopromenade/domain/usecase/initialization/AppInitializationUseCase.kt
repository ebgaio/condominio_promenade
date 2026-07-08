package br.com.edificiopromenade.domain.usecase.initialization

import br.com.edificiopromenade.domain.model.AppInitializationResult
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentosUseCase
import br.com.edificiopromenade.domain.usecase.condominio.ConsultarCondominioAtivoUseCase
import br.com.edificiopromenade.domain.usecase.morador.ListarMoradoresAtivosUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class AppInitializationUseCase @Inject constructor(
    private val consultarCondominioAtivoUseCase: ConsultarCondominioAtivoUseCase,
    private val consultarApartamentosUseCase: ConsultarApartamentosUseCase,
    private val listarMoradoresAtivosUseCase: ListarMoradoresAtivosUseCase
) {

    suspend operator fun invoke(): AppInitializationResult {

        val condominio = consultarCondominioAtivoUseCase().first()
            ?: return AppInitializationResult.NeedCondominio

        val apartamentos = consultarApartamentosUseCase().first()
        if (apartamentos.isEmpty()) {
            return AppInitializationResult.NeedApartamento
        }

        val moradores = listarMoradoresAtivosUseCase().first()
        if (moradores.isEmpty()) {
            return AppInitializationResult.NeedMorador
        }

        return AppInitializationResult.Ready
    }
}