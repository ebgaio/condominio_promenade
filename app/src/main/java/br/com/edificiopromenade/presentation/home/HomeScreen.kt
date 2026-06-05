package br.com.edificiopromenade.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onMoradoresClick: () -> Unit,
    onCondominioClick: () -> Unit,
    onApartamentosClick: () -> Unit
) {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sistema Edifício Promenade",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = {}) {
                Text("Novo Fechamento")
            }

            Button(onClick = {}) {
                Text("Histórico")
            }

            Button(
                onClick = onMoradoresClick
            ) {
                Text("Moradores")
            }

            Button(
                onClick = onCondominioClick
            ) {
                Text("Condomínio")
            }

            Button(
                onClick = onApartamentosClick
            ) {
                Text("Apartamentos")
            }

            Button(onClick = {}) {
                Text("Configurações")
            }

            Text("Versão 1.0")
        }
    }
}