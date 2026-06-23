package br.com.edificiopromenade.presentation.fechamento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
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
import androidx.navigation.NavController
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.navigation.AppDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovoFechamentoScreen(
    navController: NavController,
    viewModel: NovoFechamentoViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold (
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Novo Fechamento",
                style =
                    MaterialTheme
                        .typography
                        .headlineSmall
            )

            state.mensagem?.let { mensagem ->

                InlineMessageBanner (
                    message = when (mensagem) {
                        is UiMessage.Success -> mensagem.text
                        is UiMessage.Error -> mensagem.text
                    },
                    onDismiss = {
                        viewModel.limparMensagem()
                    }
                )
            }

            ExposedDropdownMenuBox(

                expanded = state.expandirMeses,

                onExpandedChange = {
                    viewModel.alterarExpandirMeses(it)
                }

            ) {

                OutlinedTextField(
                    value = state.mes,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Mês")
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = state.expandirMeses,
                    onDismissRequest = {
                        viewModel.alterarExpandirMeses(false)
                    }
                ) {
                    state.meses.forEach { mes ->
                        DropdownMenuItem(
                            text = {
                                Text(mes)
                            },
                            onClick = {
                                viewModel.selecionarMes(mes)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.ano,
                onValueChange = viewModel::onAnoChanged,
                label = {
                    Text("Ano")
                },
                keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
            )

            OutlinedTextField(
                value = state.fundoReserva,
                onValueChange = viewModel::onFundoReservaChanged,
                label = {
                    Text("Fundo de Reserva")
                },
                keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
            )

            OutlinedTextField(
                value = state.decimoTerceiroFerias,
                onValueChange = viewModel::onDecimoTerceiroChanged,

                label = {
                    Text("13º / Férias")
                },
                keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
            )
            val formularioValido = state.mes.isNotBlank()
                    && state.ano.length == 4

            Button (
                enabled = formularioValido,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.salvar()
                }
            ) {
                Text("Criar")
            }

            HorizontalDivider()

            Text(
                "Fechamentos"
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.fechamentos) {
                    Card (
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    navController.navigate(
                                        AppDestinations.despesasRoute(
                                            it.id
                                        )
                                    )
                                }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                "Competência  ${it.mes.toString()
                                    .padStart(2, '0')
                                }/${it.ano}"
                            )

                            Text(
                                if (it.finalizado)
                                    "Finalizado"
                                else
                                    "Aberto"
                            )
                        }
                    }
                }
            }
        }
    }
}