package br.com.edificiopromenade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.edificiopromenade.presentation.navigation.AppNavHost
import br.com.edificiopromenade.presentation.theme.SistemaEdificioPromenadeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            SistemaEdificioPromenadeTheme {

                AppNavHost()

            }
        }
    }
}