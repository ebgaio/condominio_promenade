package br.com.edificiopromenade.presentation.util

import java.math.BigDecimal
import java.math.RoundingMode

object FinanceUtils {

    fun money(
        valor: Double
    ): BigDecimal =
        BigDecimal.valueOf(valor)

    fun round(
        valor: BigDecimal
    ): BigDecimal =
        valor.setScale(
            2,
            RoundingMode.HALF_UP
        )
}