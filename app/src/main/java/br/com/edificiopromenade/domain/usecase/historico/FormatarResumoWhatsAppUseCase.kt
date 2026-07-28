package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi
import jakarta.inject.Inject
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class FormatarResumoWhatsAppUseCase @Inject constructor(
    private val formatarTextoWhatsAppUseCase: FormatarTextoWhatsAppUseCase
) {

    operator fun invoke(
        demonstrativo: DemonstrativoHistoricoUi,
        mes: Int,
        ano: Int
    ): String {

        val mesNome =
            Month.of(mes)
                .getDisplayName(
                    TextStyle.FULL,
                    Locale("pt", "BR")
                )

        return formatarTextoWhatsAppUseCase(
            demonstrativo = demonstrativo,
            mesNome = mesNome,
            ano = ano
        )
    }
}