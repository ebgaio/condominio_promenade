package br.com.edificiopromenade.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun formatarMoeda(
    valor: Double
): String {

    return NumberFormat
        .getCurrencyInstance(
            Locale.forLanguageTag("pt_br")
        )
        .format(valor)
}