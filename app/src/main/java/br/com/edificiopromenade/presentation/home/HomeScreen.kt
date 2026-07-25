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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage

@Composable
fun HomeScreen(
    onMoradoresClick: () -> Unit,
    onCondominioClick: () -> Unit,
    onApartamentosClick: () -> Unit,
    onNovoFechamentoClick: () -> Unit,
    onHistoricoClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        lifecycleOwner
    ) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (
                    event == Lifecycle.Event.ON_RESUME
                    ) {
                    viewModel.carregar()
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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

            Button(
                onClick = {
                    if (viewModel.podeAbrirNovoFechamento()) {
                        onNovoFechamentoClick()
                    } else {
                        viewModel.mostrarMensagem(
                        viewModel.mensagemNovoFechamento()
                        )
                    }
                },
                enabled = !state.carregando
            ) {
                Text("Novo Fechamento")
            }

            Button(
                onClick = onHistoricoClick
            ) {
                Text("Histórico")
            }

            Button(
                onClick = {
                    if (viewModel.podeAbrirMoradores()) {
                        onMoradoresClick()
                    } else {
                        viewModel.mostrarMensagem(
                            viewModel.mensagemMoradores()
                        )
                    }
                },
                enabled = !state.carregando
            ) {
                Text("Moradores")
            }

            Button(
                onClick = onCondominioClick
            ) {
                Text("Condomínio")
            }

            Button(
                onClick = {
                    if (viewModel.podeAbrirApartamentos()) {
                        onApartamentosClick()
                    } else {
                        viewModel.mostrarMensagem(
                        viewModel.mensagemApartamentos()
                        )
                    }
                },
                enabled = !state.carregando
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