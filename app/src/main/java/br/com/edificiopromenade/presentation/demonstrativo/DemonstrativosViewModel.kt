package br.com.edificiopromenade.presentation.demonstrativo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.demonstrativo.ConsultarDemonstrativosPorFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.demonstrativo.ConsultarDemonstrativosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemonstrativosViewModel @Inject constructor(
    private val consultarDemonstrativosPorFechamentoUseCase: ConsultarDemonstrativosPorFechamentoUseCase
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
                consultarDemonstrativosPorFechamentoUseCase(
                    fechamentoId
                )

            _uiState.value =
                DemonstrativosUiState(
                    demonstrativos = lista,

                    totalGeral =
                        lista.sumOf {
                            it.total
                        }
                )
        }
    }
}