package br.com.edificiopromenade.presentation.history

import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi

data class HistoryUiState(
    val selectedYear: Int = 2026,
    val selectedMonth: Int = 1,
    val searchByMonth: Boolean = true,
    val selectedApartmentId: Long? = null,
    val demonstrativos: List<DemonstrativoHistoricoUi> = emptyList(),
    val apartments: List<Pair<Long, String>> = emptyList(),
    val totalGeral: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
