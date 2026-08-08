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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarMoeda


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DespesaScreen(
    fechamentoId: Long,
    onVoltar: () -> Unit,
    onAbrirDemonstrativos: (Long) -> Unit,
    onAbrirItensDespesa: (Long) -> Unit,
    onAdicionarDespesaAvulsa: (Long) -> Unit,
    viewModel: DespesaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

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
                    Text("Deseja realmente excluir a despesa '${despesa.descricao}'?")
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
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Despesas do Fechamento",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    if (state.competencia.isNotBlank()) {
                        Text(
                            text = "• ${state.competencia}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                if (state.fechamentoFinalizado) {
                    Text(
                        text = "FECHAMENTO FINALIZADO",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

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

                ExposedDropdownMenuBox(
                    expanded = state.expandirTipos,

                    onExpandedChange = {
                        viewModel.alterarExpandido(it)
                    }
                ) {
                    OutlinedTextField(
                        enabled = !state.fechamentoFinalizado,
                        value = state.tipoSelecionado?.descricao ?: "",
                        onValueChange = {},
                        readOnly = true,

                        label = {
                            Text("Tipo de Despesa")
                        },

                        modifier = Modifier
                            .menuAnchor(
                                ExposedDropdownMenuAnchorType.PrimaryEditable,
                                enabled = !state.fechamentoFinalizado
                            )
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
                    enabled = !state.fechamentoFinalizado,
                    value = state.valor,
                    onValueChange = viewModel::onValorChanged,

                    label = {
                        Text("Valor")
                    },
                    placeholder = { Text("0,00") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        enabled = !state.fechamentoFinalizado,
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            viewModel.salvar()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Adicionar")
                    }

                    Button(
                        enabled = !state.fechamentoFinalizado,
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()

                            onAdicionarDespesaAvulsa(
                                fechamentoId
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Adicionar Avulsa")
                    }
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

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            onAbrirItensDespesa(despesa.id)
                                        }
                                    ) {
                                        Text("Itens")
                                    }

                                    Button(
                                        enabled = !state.fechamentoFinalizado,
                                        onClick = {
                                            focusManager.clearFocus()
                                            keyboardController?.hide()
                                            viewModel.solicitarExclusao(despesa)
                                        }
                                    ) {
                                        Text("Excluir")
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()

                Text(
                    text = "Total: ${formatarMoeda(total)}",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!state.fechamentoFinalizado) {
                        Button(
                            modifier = Modifier.width(250.dp),
                            onClick = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                viewModel.finalizarFechamento {
                                    onAbrirDemonstrativos(
                                        fechamentoId
                                    )
                                }
                            }
                        ) {
                            Text("Finalizar Fechamento")
                        }
                    } else {
                        Button(
                            modifier = Modifier.width(250.dp),
                            onClick = {
                                onAbrirDemonstrativos(
                                    fechamentoId
                                )
                            }
                        ) {
                            Text("Ver Demonstrativos")
                        }
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onVoltar
                    ) {
                        Text("Voltar")
                    }
                }
            }
        }
    }
}