package br.com.edificiopromenade.presentation.util

import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {

    fun format(input: String): String {
        val digits = input.filter { it.isDigit() }
        if (digits.isEmpty()) return ""

        val value = digits.toBigDecimal().divide(java.math.BigDecimal(100))

        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt_br"))
            .apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
            .format(value)
            .replace("R$", "")
            .trim()
    }
}