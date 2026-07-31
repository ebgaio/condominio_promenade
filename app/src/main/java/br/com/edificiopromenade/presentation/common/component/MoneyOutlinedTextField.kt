package br.com.edificiopromenade.presentation.common.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import br.com.edificiopromenade.presentation.util.MoneyFormatter

private const val MAX_MONEY_LENGTH = 12

@Composable
fun MoneyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "0,00"
) {

    var textFieldValue by remember {

        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(
                    value.length
                )
            )
        )
    }

    var limiteAtingido by remember {
        mutableStateOf(false)
    }

    /*
     * Sincroniza alterações externas no valor.
     */
    LaunchedEffect(value) {

        if (
            value != textFieldValue.text
        ) {

            textFieldValue =
                TextFieldValue(
                    text = value,
                    selection = TextRange(
                        value.length
                    )
                )

            limiteAtingido = false
        }
    }

    OutlinedTextField(

        value = textFieldValue,

        onValueChange = { novoValor ->

            /*
             * Valor atualmente aceito.
             */
            val textoAnterior =
                textFieldValue.text

            /*
             * Extrai somente os dígitos
             * do valor novo.
             */
            val digitsNovo =
                novoValor.text.filter {
                    it.isDigit()
                }

            /*
             * Formata o novo valor.
             */
            val valorFormatado =
                MoneyFormatter.format(
                    digitsNovo
                )

            /*
             * Verifica o limite.
             */
            if (
                valorFormatado.length >
                MAX_MONEY_LENGTH
            ) {

                /*
                 * Não altera o valor aceito.
                 */
                textFieldValue =
                    TextFieldValue(
                        text = textoAnterior,
                        selection = TextRange(
                            textoAnterior.length
                        )
                    )

                /*
                 * Ativa a mensagem.
                 */
                limiteAtingido = true

                /*
                 * Não envia valor inválido
                 * para o ViewModel.
                 */
                return@OutlinedTextField
            }

            /*
             * Valor válido.
             */
            limiteAtingido = false

            textFieldValue =
                TextFieldValue(
                    text = valorFormatado,
                    selection = TextRange(
                        valorFormatado.length
                    )
                )

            onValueChange(
                valorFormatado
            )
        },

        label = {
            Text(label)
        },

        placeholder = {
            Text(placeholder)
        },

        isError = limiteAtingido,

        supportingText = {

            if (limiteAtingido) {

                Text(
                    "Valor máximo permitido: 9.999.999,99"
                )
            }
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),

        modifier = modifier
            .onPreviewKeyEvent { event ->

                /*
                 * Intercepta o Backspace ANTES
                 * do OutlinedTextField.
                 */
                if (
                    event.key == Key.Backspace &&
                    event.type == KeyEventType.KeyDown &&
                    limiteAtingido
                ) {

                    /*
                     * Apenas remove a mensagem.
                     */
                    limiteAtingido = false

                    /*
                     * O valor true informa ao Compose
                     * que o evento foi consumido.
                     *
                     * Portanto o Backspace NÃO
                     * será executado pelo TextField.
                     */
                    true

                } else {

                    /*
                     * Outros eventos seguem
                     * normalmente para o TextField.
                     */
                    false
                }
            }
    )
}
