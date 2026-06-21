package br.com.edificiopromenade.presentation.util

import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {

    fun format(input: String): String {

        val digits =
            input.filter {
                it.isDigit()
            }

        if (digits.isEmpty()) {
            return ""
        }

        val value = digits.toLong() / 100.0

        return NumberFormat
            .getNumberInstance(
                Locale("pt", "BR")
            )
            .apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
            .format(value)
    }
}