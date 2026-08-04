package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import jakarta.inject.Inject

class CadastrarTipoDespesaUseCase @Inject constructor(
    private val repository: TipoDespesaRepository
) {

    suspend operator fun invoke(
        tipo: TipoDespesaEntity
    ): ResultadoCadastroTipoDespesa {

        val descricao = tipo.descricao.trim()

        if (descricao.isBlank()) {
            return ResultadoCadastroTipoDespesa.DescricaoInvalida
        }

        val existe = repository.existeAtivoComDescricao(
            descricao
        )

        if (existe) {
            return ResultadoCadastroTipoDespesa.Duplicado
        }

        val tipoNormalizado = tipo.copy(
            descricao = descricao
        )

        val id = repository.insert(
            tipoNormalizado
        )

        return ResultadoCadastroTipoDespesa.Sucesso(
            id = id
        )
    }
}

sealed interface ResultadoCadastroTipoDespesa {

    data class Sucesso(
        val id: Long
    ) : ResultadoCadastroTipoDespesa

    data object DescricaoInvalida : ResultadoCadastroTipoDespesa

    data object Duplicado : ResultadoCadastroTipoDespesa
}
