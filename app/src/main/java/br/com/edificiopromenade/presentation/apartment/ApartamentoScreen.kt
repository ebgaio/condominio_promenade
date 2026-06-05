package br.com.edificiopromenade.presentation.apartment

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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
        }
    }
}