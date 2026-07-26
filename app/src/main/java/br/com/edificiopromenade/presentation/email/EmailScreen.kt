package br.com.edificiopromenade.presentation.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailScreen(
    fechamentoId: Long,
    onVoltar: () -> Unit,
    viewModel: EmailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    state.email ?: return

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
                    text = state.email!!.competencia
                )

                Text(
                    text = state.email!!.assunto
                )

                Text(
                    text =
                        if(state.email!!.destinatario.isBlank())
                            "(não configurado)"
                        else
                            state.email!!.destinatario
                )

                Button(
                    onClick = onVoltar
                ) {
                    Text("Voltar")
                }

                Button(
                    onClick = onVoltar
                ) {
                    TODO("Integração Gmail API")
                }
            }
        }
    }
}