package br.com.edificiopromenade.presentation.apartment

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.util.formatarFracao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartamentoScreen(
    viewModel: ApartamentoViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Cadastro de Apartamentos",
                style = MaterialTheme.typography
                        .headlineSmall
            )

            OutlinedTextField(

                value = uiState.numero,

                onValueChange = viewModel::onNumeroChanged,

                label = {
                    Text("Número")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(

                value = uiState.fracaoIdealAtual,

                onValueChange = viewModel::onFracaoChanged,

                label = {
                    Text("Fração Ideal")
                },

                modifier =Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.salvar()
                }
            ) {

                Text("Salvar")
            }

            HorizontalDivider()

            Text(
                text = "Apartamentos Cadastrados",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    "Qtd: ${uiState.apartamentos.size}"
            )

            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {

                items(
                    items = uiState.apartamentos,
                    key = { it.id }
                ) { apartamento ->

                    Card(
                        //modifier = Modifier.fillMaxWidth()
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "Apartamento ${apartamento.numero}"
                            )

                            Text(
                                text =
                                    "Fração: ${
                                        formatarFracao(
                                            apartamento.fracaoIdealAtual
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