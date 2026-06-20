package br.com.edificiopromenade.presentation.common.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TopMessageBanner(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    LaunchedEffect (message) {
        delay(3000.milliseconds)
        onDismiss()
    }

    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable() {
                onDismiss()
            }
    ) {

        Text(
            text = "✓ $message",
            modifier = Modifier.padding(16.dp)
        )
    }
}