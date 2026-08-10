package br.com.edificiopromenade.presentation.despesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.model.CadastrarDespesaCommand
import br.com.edificiopromenade.domain.model.ExcluirDespesaCommand
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.despesa.CadastrarDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ConsultarDespesasPorFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ExcluirDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.VerificarDespesaExistenteUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.FinalizarFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.ConsultarTiposDespesaUiUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesa.mapper.toUi
import br.com.edificiopromenade.presentation.despesa.model.DespesaUi
import br.com.edificiopromenade.presentation.tipodespesa.model.TipoDespesaUi
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DespesaViewModel @Inject constructor(
    private val cadastrarDespesaUseCase: CadastrarDespesaUseCase,
    private val consultarDespesasPorFechamentoUseCase: ConsultarDespesasPorFechamentoUseCase,
    private val excluirDespesaUseCase: ExcluirDespesaUseCase,
    private val consultarTiposDespesaUiUseCase: ConsultarTiposDespesaUiUseCase,
    private val finalizarFechamentoUseCase: FinalizarFechamentoUseCase,
    private val verificarDespesaExistenteUseCase: VerificarDespesaExistenteUseCase,
    private val fechamentoRepository: FechamentoRepository,
    private val despesaRepository: DespesaRepository,
    private val despesaTemporariaParcelaRepository: DespesaTemporariaParcelaRepository,
    private val despesaTemporariaRepository: DespesaTemporariaRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaUiState()
        )

    val uiState = _uiState.asStateFlow()

    private var fechamentoId: Long = 0

    fun carregar(
        fechamentoId: Long
    ) {
        this.fechamentoId = fechamentoId

        viewModelScope.launch {
            val fechamento = fechamentoRepository.findById(
                    fechamentoId
                )
                ?: return@launch

            val fechamentoAnterior =
                fechamentoRepository.findAnterior(
                    mes = fechamento.mes,
                    ano = fechamento.ano
                )

            val fechamentoProximo =
                fechamentoRepository.findProximo(
                    mes = fechamento.mes,
                    ano = fechamento.ano
                )

            _uiState.value =
                _uiState.value.copy(

                    fechamentoAtualId = fechamento.id,
                    fechamentoFinalizado = fechamento.finalizado,

                    competencia = "${fechamento.mes.toString().padStart(
                                2, '0')}/${fechamento.ano}",

                    fechamentoAnteriorId = fechamentoAnterior?.id,
                    fechamentoProximoId = fechamentoProximo?.id
                )
        }

        viewModelScope.launch {
            consultarDespesasPorFechamentoUseCase(
                fechamentoId
            ).collect { lista ->

                val despesasComItens = despesaRepository
                        .findComItensListByFechamento(
                            fechamentoId
                        )

                val idsComItens = despesasComItens
                        .filter { it.itens.isNotEmpty()}
                        .map {it.despesa.id}
                        .toSet()

                _uiState.value =
                    _uiState.value.copy(

                        despesas = lista.map { despesaComTipo ->
                                despesaComTipo
                                    .toUi()
                                    .copy(
                                        possuiItens = despesaComTipo.despesa.id in idsComItens
                                    )
                            }
                    )

//                _uiState.value =
//                    _uiState.value.copy(
//                        despesas = lista.map {
//                            it.toUi()
//                        }
//                    )
            }
        }

        viewModelScope.launch {
            consultarTiposDespesaUiUseCase()
                .collect { listaUi ->

                    _uiState.value =
                        _uiState.value.copy(
                            tiposDespesa = listaUi,
                            tipoSelecionado = _uiState.value.tipoSelecionado
                                    ?: listaUi.firstOrNull()
                        )
                }
        }
    }

    fun editarDespesa(
        despesa: DespesaUi
    ) {

        if (_uiState.value.fechamentoFinalizado) {
            return
        }

        viewModelScope.launch {

            val despesaComItens = despesaRepository.findComItens(
                    despesa.id
                )

            if (
                despesaComItens != null &&
                despesaComItens.itens.isNotEmpty()
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Esta despesa possui itens. Altere os valores pela tela Itens."
                        )
                    )
                return@launch
            }

            var descricaoParaEdicao = ""

            if (despesa.avulsa) {

                val parcela =
                    despesaTemporariaParcelaRepository
                        .findByDespesaId(
                            despesa.id
                        )

                if (parcela != null) {

                    val despesaTemporaria =
                        despesaTemporariaRepository.findById(
                            parcela.despesaTemporariaId
                        )

                    descricaoParaEdicao =
                        despesaTemporaria?.descricao
                            ?: despesa.descricao
                }
            }

            _uiState.value =
                _uiState.value.copy(
                    despesaEmEdicao = despesa,
                    descricaoLivre = descricaoParaEdicao,
                    valor = MoneyFormatter.format(
                            (despesa.valor * 100)
                                .toLong()
                                .toString()
                        ),
                    mensagem = null,
                    expandirTipos = false
                )
        }
    }

    fun cancelarEdicao() {

        _uiState.value =
            _uiState.value.copy(
                despesaEmEdicao = null,
                valor = "",
                descricaoLivre = "",
                mensagem = null
            )
    }

    fun abrirFechamentoAnterior(
        onAbrir: (Long) -> Unit
    ) {
        val id = _uiState.value.fechamentoAnteriorId
                ?: return
        onAbrir(id)
    }

    fun abrirFechamentoProximo(
        onAbrir: (Long) -> Unit
    ) {
        val id = _uiState.value.fechamentoProximoId
                ?: return
        onAbrir(id)
    }

    fun onValorChanged(
        valor: String
    ) {
        val cleanValor = valor.filter { it.isDigit() }
        if (cleanValor.length > 12) return

        _uiState.value =
            _uiState.value.copy(
                valor = MoneyFormatter.format(
                    cleanValor
                )
            )
    }

    fun onDescricaoChanged(
        descricao: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                descricaoLivre = descricao
            )
    }

    fun salvar() {
        viewModelScope.launch {

            val valorDespesa = stateToDouble()

            if (valorDespesa == null || valorDespesa <= 0.0) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido para a despesa."
                        )
                    )
                return@launch
            }

            val despesaEmEdicao = _uiState.value.despesaEmEdicao

            if (despesaEmEdicao != null) {

                if (
                    despesaEmEdicao.avulsa &&
                    _uiState.value.descricaoLivre.isBlank()
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            mensagem = UiMessage.Error(
                                "Informe o nome da despesa avulsa."
                            )
                        )
                    return@launch
                }

                val entidade =
                    despesaRepository.findById(
                        despesaEmEdicao.id
                    )
                        ?: return@launch

                var novaDescricaoLivre = entidade.descricaoLivre

                if (despesaEmEdicao.avulsa) {

                    val parcela =
                        despesaTemporariaParcelaRepository
                            .findByDespesaId(
                                entidade.id
                            )

                    if (parcela != null) {

                        val despesaTemporaria =
                            despesaTemporariaRepository.findById(
                                parcela.despesaTemporariaId
                            )

                        if (despesaTemporaria != null) {

                            val novoNome =
                                _uiState.value
                                    .descricaoLivre
                                    .trim()

                            despesaTemporariaRepository.update(
                                despesaTemporaria.copy(
                                    descricao = novoNome
                                )
                            )

                            novaDescricaoLivre =
                                if (
                                    despesaTemporaria.quantidadeParcelas > 1
                                ) {
                                    "$novoNome - Parcela " +
                                            "${parcela.numeroParcela}/" +
                                            "${despesaTemporaria.quantidadeParcelas}"
                                } else {
                                    novoNome
                                }
                        }
                    }
                }

                despesaRepository.update(
                    entidade.copy(
                        descricaoLivre =
                            novaDescricaoLivre,
                        valor =
                            valorDespesa
                    )
                )

                despesaTemporariaParcelaRepository
                    .atualizarValorPorDespesaId(
                        despesaId = entidade.id,
                        valor = valorDespesa
                    )

//                if (despesaEmEdicao != null) {
//                    val entidade = despesaRepository.findById(
//                        despesaEmEdicao.id
//                    )
//                        ?: return@launch
//
//                    despesaRepository.update(
//                        entidade.copy(
//                            valor = valorDespesa
//                        )
//                    )
//
//                    despesaTemporariaParcelaRepository
//                        .atualizarValorPorDespesaId(
//                            despesaId = entidade.id,
//                            valor = valorDespesa
//                        )
//
//                    _uiState.value =
//                        _uiState.value.copy(
//                            despesaEmEdicao = null,
//                            valor = "",
//                            mensagem = UiMessage.Success(
//                                "Despesa atualizada com sucesso."
//                            )
//                        )
//                    return@launch
//                }
            }

            val descricao = _uiState.value.descricaoLivre.trim()

            if (
                verificarDespesaExistenteUseCase(
                    fechamentoId,
                    _uiState.value.tipoSelecionado!!.id

                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                        "A despesa '$descricao' já foi cadastrada."
                        )
                    )
                return@launch
            }

            cadastrarDespesaUseCase(
                CadastrarDespesaCommand(
                    fechamentoId = fechamentoId,
                    tipoDespesaId = _uiState.value.tipoSelecionado!!.id,
                    descricaoLivre = _uiState.value.descricaoLivre,
                    valor = valorDespesa
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    descricaoLivre = ""
                )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    mensagem = UiMessage.Success(
                     "Despesa cadastrada com sucesso."
                    )
                )
            return@launch
        }
    }

    private fun stateToDouble(): Double? {
        return _uiState.value.valor
            .replace(".", "")
            .replace(",", ".")
            .toDoubleOrNull()
    }

    fun solicitarExclusao(
        despesa: DespesaUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = despesa
            )
    }

    fun cancelarExclusao() {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = null
            )
    }

    fun confirmarExclusao() {
        val despesa = _uiState.value.despesaSelecionadaParaExclusao
                ?: return

        viewModelScope.launch {
            excluirDespesaUseCase(
                ExcluirDespesaCommand(
                    despesa.id
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    despesaSelecionadaParaExclusao = null,
                    mensagem = UiMessage.Success(
                     "Despesa excluída com sucesso."
                    )
                )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null,
                valor = "",
                descricaoLivre = ""
            )
    }

    fun alterarExpandido(
        expandido: Boolean
    ) {
        if (_uiState.value.despesaEmEdicao != null) {
            return
        }
        _uiState.value =
            _uiState.value.copy(
                expandirTipos = expandido
            )
    }

    fun selecionarTipo(
        tipo: TipoDespesaUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                tipoSelecionado = tipo,
                expandirTipos = false
            )
    }

    fun finalizarFechamento(
        onSucesso: () -> Unit
    ) {
        viewModelScope.launch {

            if (_uiState.value.despesas.isEmpty()) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Cadastre pelo menos uma despesa antes de finalizar o fechamento."
                        )
                    )
                return@launch
            }

            finalizarFechamentoUseCase(
                fechamentoId
            )
            onSucesso()
        }
    }
}