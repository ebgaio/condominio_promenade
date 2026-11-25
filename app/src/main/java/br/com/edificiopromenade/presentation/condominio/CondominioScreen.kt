package br.com.edificiopromenade.presentation.condominio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.maxLengthTrim
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import br.com.edificiopromenade.domain.model.Cnpj
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.CnpjOutputTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CondominioScreen(
    modoInicializacao: Boolean = false,
    onProximo: () -> Unit = {},
    onSair: () -> Unit = {},
    onConcluido: () -> Unit = {},
    viewModel: CondominioViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val cnpjState = rememberTextFieldState(state.condominio.cnpj)

    // Teclado numérico por padrão; alfanumérico apenas sob demanda
    var tecladoAlfanumerico by rememberSaveable { mutableStateOf(false) }

    // ViewModel -> campo (ex.: carregamento assíncrono do condomínio)
    LaunchedEffect(state.condominio.cnpj) {
        if (cnpjState.text.toString() != state.condominio.cnpj) {
            cnpjState.setTextAndPlaceCursorAtEnd(state.condominio.cnpj)
        }
    }

    // Campo -> ViewModel
    LaunchedEffect(cnpjState) {
        snapshotFlow { cnpjState.text.toString() }
            .collect { cnpj ->
                if (cnpj != viewModel.uiState.value.condominio.cnpj) {
                    viewModel.onCnpjChanged(cnpj)
                }
            }
    }

    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Cadastro do Condomínio",
                style = MaterialTheme.typography.headlineSmall
            )

            state.mensagem?.let { mensagem ->

                InlineMessageBanner(
                    message = when (mensagem) {
                        is UiMessage.Success -> mensagem.text
                        is UiMessage.Error -> mensagem.text
                    },
                    onDismiss = {
                        viewModel.limparMensagem()
                    }
                )
            }

            OutlinedTextField(
                value = state.condominio.nome,
                onValueChange = viewModel::onNomeChanged,
                label = {
                    Text("Nome")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                state = cnpjState,
                label = {
                    Text("CNPJ")
                },
                placeholder = {
                    Text("00.000.000/0000-00 ou 12.ABC.345/01DE-35")
                },
                trailingIcon = {
                    // Alterna o teclado somente quando o usuário desejar
                    TextButton(
                        onClick = {
                            tecladoAlfanumerico = !tecladoAlfanumerico
                        }
                    ) {
                        Text(if (tecladoAlfanumerico) "123" else "ABC")
                    }
                },
                keyboardOptions =
                    if (tecladoAlfanumerico) {
                        KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrectEnabled = false
                        )
                    } else {
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    },
                inputTransformation = CnpjAlfanumericoInput.then(
                    InputTransformation.maxLengthTrim(Cnpj.TAMANHO)
                ),
                outputTransformation = CnpjOutputTransformation(),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.condominio.endereco,
                onValueChange = viewModel::onEnderecoChanged,
                label = {
                    Text("Endereço")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value =
                    state.condominio.nomeAdministradora,
                onValueChange =
                    viewModel::onNomeAdministradoraChanged,
                label = {
                    Text("Administradora")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value =
                    state.condominio.emailAdministradora,
                onValueChange =
                    viewModel::onEmailAdministradoraChanged,
                label = {
                    Text("E-mail")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.salvar(
                        onSucesso = onConcluido
                    )
                }
            ) {
                Text("Salvar")
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {}

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = onProximo,
                ) {
                    Text("Próximo")
                }

                Button(
                    onClick = onSair
                ) {
                    Text("Sair")
                }
            }
        }
    }
}
// Converte para maiúsculas e remove máscara/caracteres inválidos
// (letras só nas 12 primeiras posições; DVs somente numéricos).
// Permite colar CNPJ já formatado, ex.: 12.ABC.345/01DE-35
private val CnpjAlfanumericoInput = InputTransformation {
    var i = 0
    while (i < length) {
        val c = charAt(i).uppercaseChar()
        if (Cnpj.permitidoNaPosicao(c, i)) {
            if (c != charAt(i)) replace(i, i + 1, c.toString())
            i++
        } else {
            delete(i, i + 1)
        }
    }
}
