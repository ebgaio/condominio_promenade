package br.com.edificiopromenade.presentation.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {

    private const val MAX_DIGITS = 12
    private val locale = Locale.forLanguageTag("pt-BR")

    /**
    * Formata uma sequência de dígitos como valor monetário.
    *
    * A estratégia utilizada considera os dois últimos
    * dígitos como casas decimais.
    *
    * Exemplos:
    *
    * "1" -> "0,01"
    * "12" -> "0,12"
    * "123" -> "1,23"
    * "1234" -> "12,34"
    * "123456" -> "1.234,56"
    */
    fun format(input: String): String {

        val digits = input.filter { it.isDigit() }

        if (digits.isEmpty()) return ""

        if (digits.length > MAX_DIGITS) {
            return format(
                digits.take(MAX_DIGITS)
            )
        }

        val value = BigDecimal(digits)
            .divide(BigDecimal(100))

        return NumberFormat.getNumberInstance(locale)
            .apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }.format(value)
    }

    /**
     * Converte um valor monetário formatado no padrão brasileiro para BigDecimal.
     *
     * Exemplos:
     *
     * 0,01" -> BigDecimal("0.01")
     * "12,34" -> BigDecimal("12.34")
     * "1.234,56" -> BigDecimal("1234.56")
     */
     fun parse(input: String): BigDecimal? {

        val digits = input.filter { it.isDigit() }

        if (digits.isEmpty()) { return null }

        if (digits.length > MAX_DIGITS) { return null }

        return BigDecimal(digits)
            .divide( BigDecimal(100) )
            .setScale( 2, RoundingMode.UNNECESSARY )
     }
}