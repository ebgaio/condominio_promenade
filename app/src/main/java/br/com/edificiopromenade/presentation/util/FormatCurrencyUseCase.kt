package com.appcar.domain.usecase

import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Caso de uso para formatação de valores monetários e numéricos.
 * Utiliza o padrão brasileiro (pt-BR):
 * - Moeda: R$ 1.234,56
 * - KM: 123,4 km
 * - Litros: 45,5 L
 */
class FormatCurrencyUseCase @Inject constructor() {

    private val brLocale = Locale.forLanguageTag("pt_br")
    private val currencyFormatter = NumberFormat.getCurrencyInstance(brLocale)
    private val numberFormatter = NumberFormat.getNumberInstance(brLocale)

    /**
     * Formata um valor como moeda brasileira (R$).
     * Exemplo: 1234.56 → "R$ 1.234,56"
     */
    fun formatCurrency(value: Double): String {
        return currencyFormatter.format(value)
    }

    /**
     * Formata um valor de quilometragem com 1 casa decimal.
     * Exemplo: 123.4 → "123,4 km"
     */
    fun formatKm(value: Double): String {
        numberFormatter.maximumFractionDigits = 1
        numberFormatter.minimumFractionDigits = 1
        return "${numberFormatter.format(value)} km"
    }

    /**
     * Formata litros com 1 casa decimal.
     * Exemplo: 45.5 → "45,5 L"
     */
    fun formatLiters(value: Double): String {
        numberFormatter.maximumFractionDigits = 1
        numberFormatter.minimumFractionDigits = 1
        return "${numberFormatter.format(value)} L"
    }

    /**
     * Formata minutos em formato legível (Xh Ymin).
     * Exemplo: 125 → "2h 5min"
     */
    fun formatMinutes(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return if (hours > 0) "${hours}h ${mins}min" else "${mins}min"
    }

    /**
     * Formata um valor de custo por km.
     * Exemplo: 0.85 → "R$ 0,85/km"
     */
    fun formatCostPerKm(value: Double): String {
        return "${formatCurrency(value)}/km"
    }
}
