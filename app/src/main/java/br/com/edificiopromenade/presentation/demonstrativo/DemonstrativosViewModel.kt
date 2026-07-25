package br.com.edificiopromenade.presentation.demonstrativo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.demonstrativo.ConsultarDemonstrativosPorFechamentoUiUseCase
import br.com.edificiopromenade.domain.usecase.email.GerarCorpoEmailHtmlUseCase
import br.com.edificiopromenade.presentation.demonstrativo.mapper.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DemonstrativosViewModel @Inject constructor(
    private val consultarDemonstrativosPorFechamentoUiUseCase: ConsultarDemonstrativosPorFechamentoUiUseCase,
    private val fechamentoRepository: FechamentoRepository,
    private val despesaRepository: DespesaRepository,
    private val gerarCorpoEmailHtmlUseCase: GerarCorpoEmailHtmlUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DemonstrativosUiState()
        )

    val uiState = _uiState.asStateFlow()

    fun carregar(
        fechamentoId: Long
    ) {

        viewModelScope.launch {

            val lista =
                consultarDemonstrativosPorFechamentoUiUseCase(
                    fechamentoId
                )

            _uiState.value =
                DemonstrativosUiState(
                    fechamentoId = fechamentoId,
                    demonstrativos = lista,

                    totalGeral =
                        lista.sumOf {
                            it.total
                        }
                )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }
}