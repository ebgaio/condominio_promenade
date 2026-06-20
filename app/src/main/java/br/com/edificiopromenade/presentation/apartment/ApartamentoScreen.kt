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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.edificiopromenade.presentation.util.formatarFracao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartamentoScreen(
    navController: NavController,
    viewModel: ApartamentoViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    LaunchedEffect(uiState.mensagem) {
        uiState.mensagem?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagem()
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
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

            OutlinedTextField(

                value = uiState.numero,
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

                value = uiState.percentualCopasa,
                onValueChange = viewModel::onFracaoChanged,
                label = {
                    Text("Percentual COPASA")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier =Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        viewModel.salvar()
                    }
                ) {
                    Text(
                        if (uiState.modoEdicao)
                            "Atualizar"
                        else
                            "Salvar"
                    )
                }

                if (uiState.modoEdicao) {

                    Button(
                        onClick = {
                            viewModel.cancelarEdicao()
                        }
                    ) {
                        Text("Voltar")
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "Apartamentos Cadastrados - " + "Qtd: ${uiState.apartamentos.size}",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {

                items(
                    items = uiState.apartamentos,
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
                                    "Fração: ${
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