package br.com.edificiopromenade.presentation.history

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.edificiopromenade.presentation.util.formatarMoeda
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onVoltar: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Rateios") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Text("Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Seletores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Seletor de Ano
                var yearExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { yearExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ano: ${state.selectedYear}")
                    }
                    DropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                        (2024..2030).forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year.toString()) },
                                onClick = {
                                    viewModel.onYearSelected(year)
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }

                // Seletor de Mês
                var monthExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { monthExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val mesNome = Month.of(state.selectedMonth).getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
                        Text("Mês: $mesNome")
                    }
                    DropdownMenu(expanded = monthExpanded, onDismissRequest = { monthExpanded = false }) {
                        (1..12).forEach { month ->
                            DropdownMenuItem(
                                text = {
                                    val name = Month.of(month).getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
                                    Text(name)
                                },
                                onClick = {
                                    viewModel.onMonthSelected(month)
                                    monthExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Tabs ou Toggle para Modo de Busca
            TabRow(selectedTabIndex = if (state.searchByMonth) 0 else 1) {
                Tab(
                    selected = state.searchByMonth,
                    onClick = { viewModel.toggleSearchMode(true) },
                    text = { Text("Por Mês") }
                )
                Tab(
                    selected = !state.searchByMonth,
                    onClick = { viewModel.toggleSearchMode(false) },
                    text = { Text("Por Apartamento") }
                )
            }

            if (!state.searchByMonth) {
                // Seletor de Apartamento
                var apExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { apExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val apLabel = state.apartments.find { it.first == state.selectedApartmentId }?.second ?: "Selecionar Apartamento"
                        Text(apLabel)
                    }
                    DropdownMenu(expanded = apExpanded, onDismissRequest = { apExpanded = false }) {
                        state.apartments.forEach { (id, numero) ->
                            DropdownMenuItem(
                                text = { Text("Apartamento $numero") },
                                onClick = {
                                    viewModel.onApartmentSelected(id)
                                    apExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.demonstrativos) { demo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Apartamento ${demo.numeroApartamento}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(onClick = {
                                        val text = viewModel.formatarParaWhatsApp(demo)
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Rateio", text)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Resumo copiado!", Toast.LENGTH_SHORT).show()
                                    }) {
                                        Text("Zap")
                                    }
                                }
                                Text(text = demo.nomeMorador)
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text("Rateio Mensal: ${formatarMoeda(demo.rateioMensal)}")
                                Text("COPASA: ${formatarMoeda(demo.copasa)}")
                                Text("Fundo Reserva: ${formatarMoeda(demo.fundoReserva)}")
                                Text("13º / Férias: ${formatarMoeda(demo.decimoTerceiroFerias)}")
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "TOTAL: ${formatarMoeda(demo.total)}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                if (state.searchByMonth) {
                    Text(
                        text = "Total Geral: ${formatarMoeda(state.totalGeral)}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}
