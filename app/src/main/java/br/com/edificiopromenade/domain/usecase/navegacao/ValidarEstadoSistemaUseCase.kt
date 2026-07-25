package br.com.edificiopromenade.domain.usecase.navegacao

import br.com.edificiopromenade.domain.navigation.EstadoSistema
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.domain.repository.CondominioRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.MoradorRepository
import jakarta.inject.Inject

class ValidarEstadoSistemaUseCase @Inject constructor(
    private val condominioRepository: CondominioRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val moradorRepository: MoradorRepository,
    private val fechamentoRepository: FechamentoRepository
) {
    suspend operator fun invoke(): EstadoSistema {

        val possuiCondominio = condominioRepository.existeCondominioAtivo()

        val possuiApartamento = apartamentoRepository.existeAlgumApartamento()

        val possuiMorador = moradorRepository.existeMoradorAtivo(true)

        val possuiFechamentoAberto = fechamentoRepository.existeFechamentoAberto(true)

        return EstadoSistema(
            possuiCondominio,
            possuiApartamento,
            possuiMorador,
            possuiFechamentoAberto
        )
    }
}