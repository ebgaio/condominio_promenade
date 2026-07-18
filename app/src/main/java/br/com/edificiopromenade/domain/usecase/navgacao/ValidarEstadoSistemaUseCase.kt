package br.com.edificiopromenade.domain.usecase.navgacao

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

        val possuiCondominio = condominioRepository.existeCondominio(1)

        val possuiApartamento = apartamentoRepository.existeApartamento("1")

        val possuiMorador = moradorRepository.existeMoradorAtivo(1)

        val possuiFechamentoAberto = fechamentoRepository.existeFechamentoAberto(1)

        return EstadoSistema(
            possuiCondominio,
            possuiApartamento,
            possuiMorador,
            possuiFechamentoAberto
        )
    }
}