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
    viewModel: MoradoresViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Moradores")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

                                Text(it.nome)
                            }
                    }
                }
            }
        }
    }
}