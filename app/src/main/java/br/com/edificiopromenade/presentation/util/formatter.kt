package br.com.edificiopromenade.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun formatarData(
    data: LocalDate?
): String {

    return data?.format(formatter)
        ?: "-"
}