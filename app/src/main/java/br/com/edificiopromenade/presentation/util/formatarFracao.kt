package br.com.edificiopromenade.presentation.util

fun formatarFracao(
    valor: Double
): String {

    return String.format("%.2f%%", valor * 100)
}