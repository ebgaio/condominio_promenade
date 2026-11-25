package br.com.edificiopromenade.presentation.util

import br.com.edificiopromenade.domain.model.Cnpj

object CnpjFormatter {

    /** Formata como XX.XXX.XXX/XXXX-DV (aceita CNPJ alfanumérico). */
    fun format(input: String): String {

        val caracteres = Cnpj.normalizar(input)

        val sb = StringBuilder()

        caracteres.forEachIndexed { index, c ->
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
