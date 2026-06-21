package br.com.edificiopromenade.presentation.common.message

sealed class UiMessage {

    abstract val text: String

    data class Success(
        override val text: String
    ) : UiMessage()

    data class Error(
        override val text: String
    ) : UiMessage()
}