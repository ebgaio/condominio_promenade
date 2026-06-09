package br.com.edificiopromenade.presentation.moradores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoradoresScreen(
    viewModel: MoradoresViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

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
                text = "Cadastro de Morador"
            )

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

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

                            Text(
                                text = "Apartamento ${item.apartamento.numero}"
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            item.moradores
                                .filter { it.ativo }
                                .forEach {

                                    TextButton(
                                        onClick = {
                                            viewModel.selecionarMorador(
                                                it.id
                                            )
                                        }
                                    ) {
                                        Text(it.nome)
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}