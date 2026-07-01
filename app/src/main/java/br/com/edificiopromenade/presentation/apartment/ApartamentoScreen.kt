package br.com.edificiopromenade.presentation.apartment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarFracao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartamentoScreen(
    navController: NavController,
    viewModel: ApartamentoViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold (
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Cadastro de Apartamentos",
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

                value = state.numero,
                onValueChange = viewModel::onNumeroChanged,
                label = {
                    Text("Número")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(

                value = state.percentualCopasa,
                onValueChange = viewModel::onFracaoChanged,
                label = {
                    Text("Percentual COPASA")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier =Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.salvar()
                    }
                ) {
                    Text(
                        if (state.modoEdicao)
                            "Atualizar"
                        else
                            "Salvar"
                    )
                }

                if (state.modoEdicao) {

                    Button(
                        onClick = {
                            viewModel.cancelarEdicao()
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "Apartamentos Cadastrados - " + "Qtd: ${state.apartamentos.size}",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {

                items(
                    items = state.apartamentos,
                    key = { it.id }
                ) { apartamento ->

                    Card(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            TextButton(
                                onClick = {
                                    navController.navigate(
                                        "apartamento_detalhe/${apartamento.id}"
                                    )
                                }
                            ) {
                                Text(
                                    text = "Apartamento ${apartamento.numero}"
                                )
                            }

                            Text(
                                text =
                                    "Fração: ${apartamento.percentualCopasa}" + " - " +
                                    "Percentual: ${
                                        formatarFracao(
                                            apartamento.percentualCopasa
                                        )
                                    }"
                            )
                        }
                    }
                }
            }
        }
    }
}