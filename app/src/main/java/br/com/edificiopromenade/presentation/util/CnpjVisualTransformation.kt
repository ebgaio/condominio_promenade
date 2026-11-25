package br.com.edificiopromenade.presentation.util

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert

/**
 * Formata visualmente os dígitos do CNPJ como 00.000.000/0000-00.
 *
 * O texto armazenado continua contendo apenas dígitos; o mapeamento
 * de cursor/seleção é feito automaticamente pelo TextFieldBuffer.
 */
class CnpjOutputTransformation : OutputTransformation {

    override fun TextFieldBuffer.transformOutput() {

        // Insere do maior índice para o menor para não deslocar
        // as posições ainda não processadas.
        SEPARADORES.forEach { (indice, simbolo) ->
            if (length > indice) {
                insert(indice, simbolo)
            }
        }
    }

    private companion object {
        val SEPARADORES = listOf(
            12 to "-",
            8 to "/",
            5 to ".",
            2 to "."
        )
    }
}
