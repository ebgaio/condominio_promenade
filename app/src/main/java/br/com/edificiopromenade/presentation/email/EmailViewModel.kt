package br.com.edificiopromenade.presentation.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.email.ConsultarDemonstrativosPorFechamentoEmailUseCase
import br.com.edificiopromenade.domain.usecase.email.GerarCorpoEmailHtmlUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.email.model.EmailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val gerarCorpoEmailHtmlUseCase: GerarCorpoEmailHtmlUseCase,
    private val fechamentoRepository: FechamentoRepository,
    private val despesaRepository: DespesaRepository,
    private val consultarDemonstrativos: ConsultarDemonstrativosPorFechamentoEmailUseCase
) : ViewModel()  {

    private val _uiState =
        MutableStateFlow(
            EmailUiState()
        )

    private var fechamentoId: Long = 0

    val uiState = _uiState.asStateFlow()

    fun carregar(
        fechamentoId: Long
    ) {

        if (_uiState.value.email == null)
            return

        _uiState.value =
            _uiState.value.copy(
                mensagem = UiMessage.Success(
                        "E-mail preparado para envio."
                    )
            )

        this.fechamentoId = fechamentoId

        viewModelScope.launch {
            val fechamento = fechamentoRepository.findById(fechamentoId)
                ?: return@launch

            val demonstrativos = consultarDemonstrativos(fechamentoId)

            val despesas = despesaRepository.findListByFechamento(fechamentoId)

            val mesNome =
                Month.of(fechamento.mes)
                    .getDisplayName(
                        TextStyle.FULL,
                        Locale.forLanguageTag("pt_br")
                    )

            val html =
                gerarCorpoEmailHtmlUseCase(
                    mesNome = mesNome,
                    ano = fechamento.ano,
                    despesas = despesas,
                    totalGeralDespesas =
                        despesas.sumOf {
                            it.valor
                        },
                    demonstrativos = demonstrativos,
                    totalGeralArrecadar =
                        demonstrativos.sumOf {
                            it.total
                        }
                )

            val email =
                EmailUi(
                    assunto = "Rateio Condomínio - $mesNome/${fechamento.ano}",
                    html = html,
                    destinatario = "",
                    competencia =
                        "$mesNome/${fechamento.ano}"
                )

            _uiState.value =
                EmailUiState(
                    fechamentoId = fechamentoId,
                    email = email
                )

//
//            _uiState.value =
//                _uiState.value.copy(
//                    fechamentoFinalizado =
//                        fechamento?.finalizado ?: false
//                )
//        }
//
//        viewModelScope.launch {
//            consultarDespesasPorFechamentoUseCase(
//                fechamentoId
//            ).collect { lista ->
//
//                _uiState.value =
//                    _uiState.value.copy(
//                        despesas = lista.map {
//                            it.toUi()
//                        }
//                    )
//            }
//        }
//
//        viewModelScope.launch {
//            consultarTiposDespesaUiUseCase()
//                .collect { listaUi ->
//
//                    _uiState.value =
//                        _uiState.value.copy(
//                            tiposDespesa = listaUi,
//                            tipoSelecionado = _uiState.value.tipoSelecionado
//                                ?: listaUi.firstOrNull()
//                        )
//                }
//        }






    }

    fun gerarEmail(
        fechamentoId: Long
    ) {
        if (_uiState.value.email == null)
            return

        _uiState.value =
            _uiState.value.copy(
                mensagem =
                    UiMessage.Success(
                        "E-mail preparado para envio."
                    )
            )
    }

//        viewModelScope.launch {
//
//            val fechamento = fechamentoRepository.findById(fechamentoId)
//                ?: return@launch
//
//            val demonstrativos = consultarDemonstrativos(fechamentoId)
//
//            val mesNome = Month.of(fechamento.mes)
//                .getDisplayName(
//                    TextStyle.FULL,
//                    Locale("pt","BR")
//                )
//
//            val html = gerarCorpoEmailHtmlUseCase(
//                mesNome = mesNome,
//                ano = fechamento.ano,
//                despesas = despesas,
//                totalGeralDespesas =
//                    despesas.sumOf {
//                        it.valor
//                    },
//                demonstrativos = demonstrativos,
//                totalGeralArrecadar =
//                    demonstrativos.sumOf {
//                        it.total
//                    }
//            )
//
//            val email = EmailUi(
//                assunto = "Rateio Condomínio - $mesNome/${fechamento.ano}",
//                html = html,
//                destinatario = "",
//                competencia = "$mesNome/${fechamento.ano}"
//                )
//
//            _uiState.update {
//                it.copy(
//                    fechamentoId = fechamentoId,
//                    email = email,
//                    carregando = false
//                )
//            }
//        }
    }
}