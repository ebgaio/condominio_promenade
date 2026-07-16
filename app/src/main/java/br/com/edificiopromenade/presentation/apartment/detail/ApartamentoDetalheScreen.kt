package br.com.edificiopromenade.presentation.apartment.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.formatarData
import br.com.edificiopromenade.presentation.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartamentoDetalheScreen(
    apartamentoId: Long,
    onVoltar: () -> Unit,
    viewModel: ApartamentoDetalheViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(apartamentoId) {
        viewModel.carregar(
            apartamentoId
        )
    }

    state.apartamento?.let { detalhe ->

        val moradoresAtivos = detalhe.moradores.filter { it.ativo }

        val moradoresHistorico = detalhe.moradores.filter { !it.ativo }

        val qtdAtivos = moradoresAtivos.size

        val qtdHistorico = moradoresHistorico.size

        val percentualOcupacao =
            if (
                detalhe.moradores.isEmpty()
            ) {
                0
            } else {
                (   qtdAtivos.toDouble()
                        /
                    detalhe.moradores.size.toDouble()
                ) * 100
            }

        val situacao =
            if (qtdAtivos > 0)
                "Ocupado"
            else
                "Vago"

        val moradorAtual = detalhe.moradores
            .firstOrNull { it.ativo }

        val diasOcupacaoAtual = moradorAtual
                ?.dataInicio
                ?.let {
                    java.time.temporal.ChronoUnit.DAYS
                        .between(
                            it,
                            java.time.LocalDate.now()
                        )
                }
                ?: 0

        val ultimaMovimentacao = detalhe.moradores
                .flatMap {
                    listOfNotNull(
                        it.dataInicio,
                        it.dataFim
                    )
                }
                .maxOrNull()

        Scaffold(
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                "Apartamento ${detalhe.apartamento.numero}",
                                style = MaterialTheme.typography.titleLarge
                            )

                            state.mensagem?.let { mensagem ->

                                InlineMessageBanner (
                                    message = when(mensagem) {
                                        is UiMessage.Success -> mensagem.text
                                        is UiMessage.Error -> mensagem.text
                                    },
                                    onDismiss = {
                                        viewModel.limparMensagem()
                                    }
                                )
                            }

                            Text(
                                text =
                                    "Percentual COPASA: ${formatarMoeda(detalhe.apartamento.percentualCopasa)}"
                            )

                            Text(
                                "Fração: ${detalhe.apartamento.percentualCopasa}"
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            HorizontalDivider()

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = "Indicadores",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "👥 Moradores Ativos: $qtdAtivos"
                            )

                            Text(
                                text = "📚 Histórico: $qtdHistorico"
                            )

                            Text(
                                text =
                                    "📁 Total Histórico Registrado: $qtdHistorico"
                            )

                            Text(
                                text = "🏠 Situação: $situacao"
                            )

                            Text(
                                text =
                                    "⏳ Ocupação Atual: $diasOcupacaoAtual dias"
                            )

                            Text(
                                text = "📊 Ocupação Histórica: ${percentualOcupacao.toInt()}%"
                            )

                            Text(
                                text =
                                    "📅 Última Movimentação: ${formatarData(ultimaMovimentacao)}"
                            )

                            Card {
                                Text(
                                    text =
                                        if (detalhe.apartamento.ativo)
                                            "✅ Apartamento Ativo"
                                        else
                                            "❌ Apartamento Inativo",

                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (!state.modoEdicao) {
                                    Button(
                                        onClick = {
                                            focusManager.clearFocus()
                                            keyboardController?.hide()
                                            viewModel.editar()
                                        }
                                    ) {
                                        Text("Editar")
                                    }
                                }
                            }

                            if (state.modoEdicao) {

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        viewModel.atualizar()
                                    }
                                ) {
                                    Text("Atualizar")
                                }

                                OutlinedTextField(

                                    value = state.numero,
                                    onValueChange = viewModel::onNumeroChanged,
                                    label = {
                                        Text("Número")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                OutlinedTextField(

                                    value = state.percentualCopasa,
                                    onValueChange = viewModel::onFracaoChanged,
                                    label = {
                                        Text("Percentual COPASA")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    )
                                )

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Resumo Executivo",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Apartamento ${detalhe.apartamento.numero}"
                            )

                            Text(
                                text = "Possui $qtdAtivos morador(es) ativo(s)"
                            )

                            Text(
                                text = "Possui $qtdHistorico registro(s) históricos"
                            )

                            Text(
                                text = "Situação atual: $situacao"
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Indicadores",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "👥 Moradores Ativos: $qtdAtivos"
                            )

                            Text(
                                text = "📚 Histórico: $qtdHistorico"
                            )

                            Text(
                                text = "🏠 Situação: $situacao"
                            )

                            Text(
                                text = "⏳ Ocupação Atual: $diasOcupacaoAtual dias"
                            )

                            Text(
                                text = "📊 Ocupação Histórica: ${percentualOcupacao.toInt()}%"
                            )

                            Text(
                                text = "📅 Última Movimentação: ${formatarData(ultimaMovimentacao)}"
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Resumo",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Moradores Ativos: $qtdAtivos"
                            )

                            Text(
                                text = "Histórico: $qtdHistorico"
                            )

                            Text(
                                text = "Situação Atual: $situacao"
                            )

                            Text(
                                text = "Tempo de Ocupação: $diasOcupacaoAtual dias"
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            val moradoresAtivos = detalhe.moradores.filter { it.ativo }

                            val moradoresEncerrados = detalhe.moradores.filter { !it.ativo }
                            
                            Text(
                                text = "Moradores Ativos",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                if (moradoresAtivos.isEmpty()) {
                                    Text(
                                        "Nenhum morador ativo"
                                    )

                                } else {
                                    moradoresAtivos.forEach {
                                        Text(
                                            text = it.nome
                                        )
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )

                                HorizontalDivider()

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )

                                Text(
                                    text = "Histórico de Moradores",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                if (moradoresEncerrados.isEmpty()) {
                                    Text(
                                        "Nenhum histórico encontrado"
                                    )

                                } else {
                                    moradoresEncerrados.forEach { morador ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp)
                                            ) {

                                                Text(
                                                    text = morador.nome
                                                )

                                                Text(
                                                    text = "Início: ${formatarData(morador.dataInicio)}"
                                                )

                                                Text(
                                                    text = "Fim: ${formatarData(morador.dataFim)}"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.inativarApartamento()
                            }
                        ) {
                            Text("Inativar")
                        }

                        Button(
                            onClick = onVoltar
                        ) {
                            Text("Voltar")
                        }
                    }
                }
            }
        }
    }
}