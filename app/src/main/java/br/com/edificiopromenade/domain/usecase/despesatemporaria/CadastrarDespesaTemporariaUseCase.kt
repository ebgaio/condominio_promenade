package br.com.edificiopromenade.domain.usecase.despesatemporaria

import br.com.edificiopromenade.data.local.entity.DespesaTemporariaEntity
import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import jakarta.inject.Inject
import java.time.LocalDate

class CadastrarDespesaTemporariaUseCase @Inject constructor(
    private val repository: DespesaTemporariaRepository
) {

    suspend operator fun invoke(
        descricao: String,
        quantidadeParcelas: Int
    ): Long {

        require(descricao.isNotBlank()) {
            "Descrição obrigatória."
        }

        require(quantidadeParcelas > 0) {
            "Quantidade de parcelas inválida."
        }

        val despesa = DespesaTemporariaEntity(
            descricao = descricao.trim(),
            quantidadeParcelas = quantidadeParcelas
        )

        return repository.insert(despesa)
    }
}