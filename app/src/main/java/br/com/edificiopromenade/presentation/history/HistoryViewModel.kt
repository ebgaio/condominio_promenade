package br.com.edificiopromenade.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.historico.ConsultarDemonstrativosPorApartamentoUiUseCase
import br.com.edificiopromenade.domain.usecase.historico.ConsultarDemonstrativosPorFechamentoUiUseCase
import br.com.edificiopromenade.domain.usecase.historico.FormatarResumoWhatsAppUseCase
import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val consultarPorFechamentoUi: ConsultarDemonstrativosPorFechamentoUiUseCase,
    private val consultarPorApartamentoUi: ConsultarDemonstrativosPorApartamentoUiUseCase,
    private val formatarResumoWhatsAppUseCase: FormatarResumoWhatsAppUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarApartamentos()
    }

    private fun carregarApartamentos() {
        viewModelScope.launch {
            val lista = apartamentoRepository.findAllAtivosList().map { it.id to it.numero }
            _uiState.update { it.copy(apartments = lista) }
        }
    }

    fun onYearSelected(year: Int) {
        _uiState.update { it.copy(selectedYear = year) }
        if (_uiState.value.searchByMonth) {
            buscarPorMes()
        }
    }

    fun onMonthSelected(month: Int) {
        _uiState.update { it.copy(selectedMonth = month) }
        if (_uiState.value.searchByMonth) {
            buscarPorMes()
        }
    }

    fun onApartmentSelected(apartmentId: Long) {
        _uiState.update { it.copy(selectedApartmentId = apartmentId, searchByMonth = false) }
        buscarPorApartamento()
    }

    fun toggleSearchMode(byMonth: Boolean) {
        _uiState.update { it.copy(searchByMonth = byMonth) }
        if (byMonth) buscarPorMes() else buscarPorApartamento()
    }

    private fun buscarPorMes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null)
            }

            val fechamento = fechamentoRepository.findByMesAno(
                    _uiState.value.selectedMonth,
                    _uiState.value.selectedYear
            )

            if (fechamento != null) {

                val demos = consultarPorFechamentoUi(fechamento.id)

                _uiState.update {
                    it.copy(
                        demonstrativos = demos,
                        totalGeral = demos.sumOf { d -> d.total },
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        demonstrativos = emptyList(),
                        totalGeral = 0.0,
                        isLoading = false,
                        errorMessage = "Nenhum fechamento encontrado para este período."
                    )
                }
            }
        }
    }

    private fun buscarPorApartamento() {
        val apartmentId = _uiState.value
            .selectedApartmentId
            ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                errorMessage = null)
            }

            val demos = consultarPorApartamentoUi(apartmentId)

            _uiState.update { it.copy(
                demonstrativos = demos,
                totalGeral = demos.sumOf { d -> d.total },
                isLoading = false
            ) }
        }
    }

    fun formatarParaWhatsApp(
        demo: DemonstrativoHistoricoUi
    ): String {

        return formatarResumoWhatsAppUseCase(
            demonstrativo = demo,
            mes = _uiState.value.selectedMonth,
            ano = _uiState.value.selectedYear
        )
    }
}
