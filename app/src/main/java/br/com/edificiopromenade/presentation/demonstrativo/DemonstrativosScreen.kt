package br.com.edificiopromenade.presentation.demonstrativo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemonstrativosScreen(
    fechamentoId: Long,
    onVoltar: () -> Unit,
    viewModel: DemonstrativosViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(fechamentoId) {
        viewModel.carregar(fechamentoId)
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Demonstrativos",
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

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.demonstrativos) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "Apartamento ${item.numeroApartamento}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = item.nomeMorador
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Text(
                                "Rateio Mensal: ${formatarMoeda(item.rateioMensal)}"
                            )

                            Text(
                                "COPASA: ${formatarMoeda(item.copasa)}"
                            )

                            Text(
                                "Fração COPASA: ${item.percentualCopasa}%"
                            )

                            Text(
                                "Fundo Reserva: ${formatarMoeda(item.fundoReserva)}"
                            )

                            Text(
                                "13º / Férias: ${formatarMoeda(item.decimoTerceiroFerias)}"
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Text(
                                text = "TOTAL: ${formatarMoeda(item.total)}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "Total Geral",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = formatarMoeda(state.totalGeral),
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onVoltar
                ) {
                    Text("Voltar")
                }

//                onEnviarEmail(fechamentoId)

//                Button(
//                    modifier = Modifier.weight(1f),
//                    onClick = {
//                        viewModel.gerarCorpoEmail { html ->
//                            // TODO: Integrar com Gmail API ou disparar Intent de e-mail
//                            // Por enquanto, mostra que gerou
//                            android.util.Log.d("EmailHTML", html)
//                        }
//                    }
//                ) {
//                    Text("Enviar E-mail")
//                }
            }
        }
    }
}