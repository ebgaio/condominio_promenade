package br.com.edificiopromenade.domain.usecase.despesatemporaria

sealed interface ResultadoCadastroDespesaTemporaria {

    data object Sucesso
        : ResultadoCadastroDespesaTemporaria

    data object DescricaoInvalida
        : ResultadoCadastroDespesaTemporaria

    data object QuantidadeParcelasInvalida
        : ResultadoCadastroDespesaTemporaria
}