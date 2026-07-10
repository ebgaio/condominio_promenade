package br.com.edificiopromenade.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.historico.ConsultarDemonstrativosPorApartamentoUiUseCase
import br.com.edificiopromenade.domain.usecase.historico.ConsultarDemonstrativosPorFechamentoUiUseCase
import br.com.edificiopromenade.domain.usecase.historico.FormatarTextoWhatsAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val consultarPorFechamentoUi: ConsultarDemonstrativosPorFechamentoUiUseCase,
    private val consultarPorApartamentoUi: ConsultarDemonstrativosPorApartamentoUiUseCase,
    private val formatarTextoWhatsAppUseCase: FormatarTextoWhatsAppUseCase
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

    fun formatarParaWhatsApp(demo: br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi): String {
        // Precisamos do mês e ano do demonstrativo. Como o demonstrativoEntity não tem mês/ano direto, 
        // em um app real buscaríamos do fechamento associado. 
        // Para simplificar neste ViewModel, usaremos os selecionados se searchByMonth for true, 
        // ou precisaremos de um novo UseCase para buscar os detalhes do fechamento.
        val mesNome = Month.of(_uiState.value.selectedMonth)
            .getDisplayName(TextStyle.FULL,
                Locale("pt", "BR")
            )
        return formatarTextoWhatsAppUseCase(
            demo, mesNome, _uiState.value.selectedYear)
    }
}
