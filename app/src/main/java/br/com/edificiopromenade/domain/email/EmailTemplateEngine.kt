package br.com.edificiopromenade.domain.email

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class EmailTemplateEngine @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun carregarTemplate(): String {
        return context.assets
            .open("templates/email_template.html")
            .bufferedReader()
            .use {
                it.readText()
            }
    }

    fun render(
        template: String,
        valores: Map<String, String>
    ): String {
        var html = template

        valores.forEach { (campo, valor) ->
            html = html.replace(
                "{{$campo}}",
                valor
            )
        }

        return html
    }
}