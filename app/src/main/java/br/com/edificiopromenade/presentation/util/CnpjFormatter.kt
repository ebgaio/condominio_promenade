package br.com.edificiopromenade.presentation.util

object CnpjFormatter {

    fun format(input: String): String {

        val digits =
            input.replace(
                Regex("[^0-9]"),
                ""
            ).take(14)

        val sb = StringBuilder()

        digits.forEachIndexed { index, c ->
            when (index) {
                2 -> sb.append(".")
                5 -> sb.append(".")
                8 -> sb.append("/")
                12 -> sb.append("-")
            }
            sb.append(c)
        }
        return sb.toString()
    }
}