package br.com.edificiopromenade.domain.usecase.apartamento

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import br.com.edificiopromenade.presentation.util.FracaoCopasaFormatter

class FracaoCopasaVisualTransformation :
    OutputTransformation {

    override fun TextFieldBuffer.transformOutput() {

        val formatted =
            FracaoCopasaFormatter.format(
                toString()
            )

        replace(0, length, formatted)
    }
}
