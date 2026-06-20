package br.com.edificiopromenade.presentation.common.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun InlineMessageBanner(
    message: String,
    onDismiss: () -> Unit
) {

    LaunchedEffect(message) {

        delay(3000)

        onDismiss()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDismiss()
            }
    ) {

        Text(
            text = "✓ $message",

            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),

            color = MaterialTheme.colorScheme.primary
        )
    }
}