package br.com.edificiopromenade.presentation.despesa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.ErrorDialog
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DespesaScreen(
    fechamentoId: Long,
    onVoltar: () -> Unit,
    onAbrirDemonstrativos: (Long) -> Unit,
    viewModel: DespesaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    state.despesaSelecionadaParaExclusao
        ?.let { despesa ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.cancelarExclusao()
                },

                title = {
                    Text("Excluir despesa")
                },

                text = {
                    Text(
                        "Deseja excluir '${despesa.descricao}'?"
                    )
                },

                confirmButton = {

                    Button(
                        onClick = {
                            viewModel.confirmarExclusao()
                        }
                    ) {
                        Text("Sim")
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            viewModel.cancelarExclusao()
                        }
                    ) {
                        Text("Não")
                    }
                }
            )
        }

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    LaunchedEffect(fechamentoId) {
        viewModel.carregar(
            fechamentoId
        )
    }

    val total =
        state.despesas.sumOf {
            it.valor
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Despesas do Fechamento",
                    style = MaterialTheme.typography.headlineSmall
                )

                state.mensagem?.let { mensagem ->

                    when (mensagem) {
                        is UiMessage.Success -> {

                            InlineMessageBanner(
                                message = mensagem.text,
                                onDismiss = {
                                    viewModel.limparMensagem()
                                }
                            )
                        }

                        is UiMessage.Error -> {

                            ErrorDialog(
                                message = mensagem.text,
                                onDismiss = {
                                    viewModel.limparMensagem()
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = state.expandirTipos,

                    onExpandedChange = {
                        viewModel.alterarExpandido(it)
                    }
                ) {
                    OutlinedTextField(

                        value = state.tipoSelecionado?.descricao ?: "",
                        onValueChange = {},
                        readOnly = true,

                        label = {
                            Text("Tipo de Despesa")
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = state.expandirTipos,

                        onDismissRequest = {
                            viewModel.alterarExpandido(false)
                        }
                    ) {
                        state.tiposDespesa.forEach { tipo ->

                            DropdownMenuItem(
                                text = {
                                    Text(tipo.descricao)
                                },

                                onClick = {
                                    viewModel.selecionarTipo(tipo)
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = state.valor,
                    onValueChange = viewModel::onValorChanged,

                    label = {
                        Text("Valor")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.salvar()
                    }
                ) {
                    Text("Adicionar")
                }

                HorizontalDivider()

                Text(
                    text = "Despesas",
                    style = MaterialTheme.typography
                        .titleMedium
                )

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.despesas,
                        key = { it.id }
                    ) { despesa ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = despesa.descricao
                                )

                                Text(
                                    text = formatarMoeda(despesa.valor)
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        viewModel.solicitarExclusao(
                                            despesa
                                        )
                                    }
                                ) {
                                    Text("Excluir")
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()

                Text(
                    text = "Total",
                    style = MaterialTheme.typography
                        .titleMedium
                )

                Text(
                    text = formatarMoeda(total),
                    style = MaterialTheme.typography
                        .titleLarge
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.finalizarFechamento {
                                onAbrirDemonstrativos(
                                    fechamentoId
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    {
                        Text("Finalizar Fechamento")
                    }

                    Button(
                        onClick = onVoltar
                    ) {
                        Text("Voltar")
                    }
                }
            }
        }
    }
}