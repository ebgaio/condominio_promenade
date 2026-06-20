package br.com.edificiopromenade.presentation.common.message

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = {},

        title = {
            Text("Erro")
        },

        text = {
            Text(message)
        },

        confirmButton = {

            TextButton (
                onClick = onDismiss
            ) {
                Text("OK")
            }
        }
    )
}