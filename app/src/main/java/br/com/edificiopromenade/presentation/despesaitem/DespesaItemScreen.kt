package br.com.edificiopromenade.presentation.despesaitem

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import br.com.edificiopromenade.presentation.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DespesaItemScreen(
    despesaId: Long,
    onVoltar: () -> Unit,
    viewModel: DespesaItemViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(despesaId) {
        viewModel.carregar(despesaId)
    }

    val total =
        state.itens.sumOf {
            it.valor
        }

    state.mensagem?.let { mensagem ->
        InlineMessageBanner(
            message = when(mensagem){
                is UiMessage.Success -> mensagem.text
                is UiMessage.Error -> mensagem.text
            },

            onDismiss = {
                viewModel.limparMensagem()
            }
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column {
                Text(
                    "Itens da Despesa",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    onClick = onVoltar
                ) {
                    Text("Voltar")
                }

                OutlinedTextField(
                    value = state.descricao,
                    onValueChange = viewModel::onDescricaoChanged,

                    label = {
                        Text("Descrição")
                    },

                    modifier = Modifier.fillMaxWidth()
                )

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
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.salvar()
                    }
                ) {
                    Text("Adicionar")
                }

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.itens,
                        key = { it.id }
                    ) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(item.descricao)
                                Text(formatarMoeda(item.valor)
                                )

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        viewModel.solicitarExclusao(item)
                                        viewModel.excluir()
                                    }
                                ) {
                                    Text("Excluir")
                                }

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        viewModel.editar(item)
                                    }
                                ) {
                                    Text("Editar")
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()

                Text("Total",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(formatarMoeda(total),
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = onVoltar
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}