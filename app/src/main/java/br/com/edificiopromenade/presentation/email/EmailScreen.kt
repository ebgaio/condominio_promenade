package br.com.edificiopromenade.presentation.email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import br.com.edificiopromenade.presentation.util.formatarMoeda

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

//                Text(
//                    "Itens da Despesa",
//                    style = MaterialTheme.typography.headlineSmall
//                )

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

//                OutlinedTextField(
//                    value = state.valor,
//                    onValueChange = viewModel::onValorChanged,
//
//                    label = {
//                        Text("Valor")
//                    },
//
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Number
//                    ),
//
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
    }
}