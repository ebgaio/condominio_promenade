package br.com.edificiopromenade.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CnpjVisualTransformation : VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val digits =
            text.text.take(14)

        val formatted =
            buildString {

                digits.forEachIndexed { index, c ->

                    when (index) {
                        2 -> append(".")
                        5 -> append(".")
                        8 -> append("/")
                        12 -> append("-")
                    }

                    append(c)
                }
            }

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {

                override fun originalToTransformed(
                    offset: Int
                ): Int {

                    val symbolsAdded = when {
                        offset <= 2 -> 0
                        offset <= 5 -> 1
                        offset <= 8 -> 2
                        offset <= 12 -> 3
                        else -> 4
                    }
                    
                    val result = offset + symbolsAdded
                    return result.coerceAtMost(formatted.length)
                }

                override fun transformedToOriginal(
                    offset: Int
                ): Int {

                    val symbolsRemoved = when {
                        offset <= 2 -> 0
                        offset <= 6 -> 1
                        offset <= 10 -> 2
                        offset <= 15 -> 3
                        else -> 4
                    }
                    
                    return (offset - symbolsRemoved).coerceAtMost(text.text.length)
                }
            }
        )
    }
}