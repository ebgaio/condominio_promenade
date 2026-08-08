package br.com.edificiopromenade.presentation.despesatemporaria

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.common.component.MoneyOutlinedTextField
import br.com.edificiopromenade.presentation.common.message.InlineMessageBanner
import br.com.edificiopromenade.presentation.common.message.UiMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DespesaTemporariaScreen(
    fechamentoId: Long,
    onVoltar: () -> Unit,
    viewModel: DespesaTemporariaViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Scaffold {

        padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            Text(

                "Nova Despesa Temporária",

                style =
                    MaterialTheme.typography.headlineSmall
            )

            state.mensagem?.let {

                InlineMessageBanner(

                    message = when(it){

                        is UiMessage.Success ->
                            it.text

                        is UiMessage.Error ->
                            it.text
                    },

                    onDismiss = {

                        viewModel.limparMensagem()

                    }
                )
            }

            OutlinedTextField(

                value = state.descricao,

                onValueChange =
                    viewModel::onDescricaoChanged,

                label = {

                    Text("Descrição")

                },

                modifier =
                    Modifier.fillMaxWidth()
            )

            OutlinedTextField(

                value = state.quantidadeParcelas,

                onValueChange =
                    viewModel::onQuantidadeParcelasChanged,

                keyboardOptions = KeyboardOptions(

                    keyboardType =
                        KeyboardType.Number

                ),

                label = {

                    Text("Quantidade de Parcelas")

                }

            )

            ExposedDropdownMenuBox(

                expanded = state.expandirMeses,

                onExpandedChange = {
                    viewModel.alterarExpandirMeses(it)
                }
            ) {

                OutlinedTextField(

                    value = state.mesInicial,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Mês inicial")
                    },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = state.expandirMeses
                        )
                    },

                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !state.carregando
                        )
                        .fillMaxWidth(),

                    enabled = !state.carregando
                )

                ExposedDropdownMenu(

                    expanded = state.expandirMeses,

                    onDismissRequest = {
                        viewModel.alterarExpandirMeses(false)
                    }
                ) {

                    state.meses.forEach { mes ->

                        DropdownMenuItem(

                            text = {
                                Text(mes)
                            },

                            onClick = {
                                viewModel.selecionarMesInicial(mes)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(

                value = state.anoInicial,

                onValueChange =
                    viewModel::onAnoInicialChanged,

                label = {
                    Text("Ano inicial")
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                modifier = Modifier.fillMaxWidth(),

                enabled = !state.carregando,

                singleLine = true
            )

            HorizontalDivider()

            Text(

                "Parcelas",

                style =
                    MaterialTheme.typography.titleMedium

            )

            LazyColumn(

                modifier =
                    Modifier.weight(1f)

            ) {

                itemsIndexed(
                    state.parcelas
                ) {

                    indice,
                    valor ->

                    MoneyOutlinedTextField(
                        value = valor,

                        onValueChange = {
                            viewModel.onValorParcelaChanged(
                                indice,
                                it
                            )
                        },

                        label = "Parcela ${indice + 1}",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TextButton(
                onClick = {
                    viewModel.adicionarParcela()
                }
            ) {
                Text("Adicionar Parcela")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(

                    modifier = Modifier.weight(1f),
                    enabled = !state.carregando,

                    onClick = {
                        viewModel.salvar(
                            fechamentoId = fechamentoId,
                            onSucesso = onVoltar
                        )
                    }
                ) {
                    Text(
                        if (state.carregando) {
                            "Salvando..."
                        } else {
                            "Salvar"
                        }
                    )
                }

                Button(

                    modifier = Modifier.weight(1f),
                    onClick =
                        onVoltar
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}