package br.com.edificiopromenade.presentation.util

object FracaoCopasaFormatter {

    fun format(
        input: String
    ): String {

        val digits =
            input.filter {
                it.isDigit()
            }
            .take(6)

        if (digits.isEmpty()) {
            return "0,00000"
        }

        val padded =
            digits.padStart(6, '0')

        return buildString {
            append(padded.first())
            append(",")
            append(padded.substring(1))
        }
    }
}