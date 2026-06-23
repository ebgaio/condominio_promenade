package br.com.edificiopromenade.domain.usecase.apartamento

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.com.edificiopromenade.presentation.util.FracaoCopasaFormatter

class FracaoCopasaVisualTransformation :
    VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val formatted =
            FracaoCopasaFormatter.format(
                text.text
            )

        return TransformedText(
            AnnotatedString(formatted),
            OffsetMapping.Identity
        )
    }
}