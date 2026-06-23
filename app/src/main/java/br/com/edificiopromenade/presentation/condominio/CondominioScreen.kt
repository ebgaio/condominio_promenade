package br.com.edificiopromenade.presentation.condominio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.CnpjVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CondominioScreen(
    viewModel: CondominioViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                value = state.nome,
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
                value = state.cnpj,
                onValueChange = {
                    viewModel.onCnpjChanged(
                        it.filter(Char::isDigit)
                    )
                },
                label = {
                    Text("CNPJ")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = CnpjVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.endereco,
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
                    state.nomeAdministradora,
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
                    state.emailAdministradora,
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
                    viewModel.salvar()
                }
            ) {
                Text("Salvar")
            }
        }
    }
}