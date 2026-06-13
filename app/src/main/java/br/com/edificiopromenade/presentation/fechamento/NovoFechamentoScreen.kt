package br.com.edificiopromenade.presentation.fechamento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovoFechamentoScreen(
    viewModel: NovoFechamentoViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(state.mensagem) {
        state.mensagem?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagem()
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(
                snackbarHostState
            )
        }

    ) { padding ->
        Column (
            modifier = Modifier
                .offset()
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

            OutlinedTextField(
                value = state.mes,
                onValueChange = viewModel::onMesChanged,

                label = {
                    Text("Mês")
                }
            )

            OutlinedTextField(
                value = state.ano,
                onValueChange = viewModel::onAnoChanged,

                label = {
                    Text("Ano")
                }
            )

            Button (
                onClick = {
                    viewModel.salvar()
                }
            ) {
                Text("Criar")
            }

            HorizontalDivider()

            Text(
                "Fechamentos"
            )

            LazyColumn() {
                items(state.fechamentos) {
                    Card (
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                "Competência ${it.mes}/${it.ano}"
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