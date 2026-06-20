package br.com.edificiopromenade.presentation.condominio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CondominioScreen(
    viewModel: CondominioViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Scaffold { it ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
        ) {

            Text(
                text = "Cadastro do Condomínio",
                style =
                    MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = state.nome,
                onValueChange =
                    viewModel::onNomeChanged,
                label = {
                    Text("Nome")
                }
            )

            OutlinedTextField(
                value = state.cnpj,
                onValueChange =
                    viewModel::onCnpjChanged,
                label = {
                    Text("CNPJ")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = state.endereco,
                onValueChange =
                    viewModel::onEnderecoChanged,
                label = {
                    Text("Endereço")
                }
            )

            OutlinedTextField(
                value =
                    state.nomeAdministradora,
                onValueChange =
                    viewModel::onNomeAdministradoraChanged,
                label = {
                    Text("Administradora")
                }
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
                )
            )

            Button(
                onClick = {
                    viewModel.salvar()
                }
            ) {
                Text("Salvar")
            }

            state.mensagem?.let {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = it
                )
            }
        }
    }
}