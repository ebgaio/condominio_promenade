package br.com.edificiopromenade.presentation.moradores

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarData
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoradoresScreen(
    viewModel: MoradoresViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var expanded by remember {
        mutableStateOf(false)
    }

    val apartamentoSelecionado =
        state.apartamentos
            .firstOrNull {
                it.apartamento.id == state.apartamentoIdSelecionado
            }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Moradores")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Cadastro de Morador",
                style = MaterialTheme.typography.headlineSmall
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

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(

                value = state.nome,
                onValueChange = viewModel::onNomeChanged,
                label = {
                    Text("Nome")
                },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            ExposedDropdownMenuBox(

                expanded = expanded,

                onExpandedChange = {
                    expanded = !expanded
                }

            ) {

                OutlinedTextField(

                    value =
                        apartamentoSelecionado
                            ?.apartamento
                            ?.numero
                            ?: "",

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Apartamento")
                    },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded = expanded
                            )
                    },

                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(

                    expanded = expanded,

                    onDismissRequest = {
                        expanded = false
                    }

                ) {

                    state.apartamentos
                        .forEach { item ->

                            DropdownMenuItem(

                                text = {

                                    Text(
                                        item.apartamento.numero
                                    )
                                },

                                onClick = {

                                    viewModel
                                        .onApartamentoSelecionado(
                                            item.apartamento.id
                                        )

                                    expanded = false
                                }
                            )
                        }
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {
                        viewModel.encerrarMorador()
                    }
                ) {
                    Text(
                        "Encerrar Morador"
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {
                        viewModel.cancelarEdicao()
                    }
                ) {
                    Text("Voltar")
                }
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(
                    items = state.apartamentos,
                    key = { it.apartamento.id }
                ) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            var mostrarHistorico by remember {
                                mutableStateOf(false)
                            }

                            Text(
                                text = "Apartamento ${item.apartamento.numero}"
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Moradores Ativos",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            val moradoresAtivos = item.moradores.filter { it.ativo }

                            val moradoresHistorico =
                                item.moradores
                                    .filter { !it.ativo }
                                    .sortedByDescending {
                                        it.dataFim ?: LocalDate.MIN
                                    }

                            moradoresAtivos.forEach { morador ->

                                TextButton(
                                    onClick = {
                                        viewModel.selecionarMorador(
                                            morador.id
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "🟢 ${morador.nome}"
                                    )
                                }
                            }

                            if (moradoresHistorico.isNotEmpty()) {

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                AssistChip(
                                    onClick = {
                                        mostrarHistorico = !mostrarHistorico
                                    },
                                    label = {

                                        Text(
                                            if (mostrarHistorico)
                                                "Ocultar Histórico"
                                            else
                                                "Mostrar Histórico"
                                        )
                                    }
                                )
                            }

                            if (
                                mostrarHistorico &&
                                moradoresHistorico.isNotEmpty()
                            ) {

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                HorizontalDivider()

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "Histórico de Moradores",
                                    style = MaterialTheme.typography.titleSmall
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                moradoresHistorico.forEach { morador ->

                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Text(
                                        text = "⚪ ${morador.nome}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Encerrado em: ${ formatarData(morador.dataFim)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}