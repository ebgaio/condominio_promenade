package br.com.edificiopromenade.presentation.util

import java.text.NumberFormat
import java.util.Locale

fun formatarMoeda(
    valor: Double
): String {

    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(valor)
}