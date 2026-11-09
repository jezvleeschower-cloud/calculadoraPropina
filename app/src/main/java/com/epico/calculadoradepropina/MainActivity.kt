package com.epico.calculadoradepropina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.epico.calculadoradepropina.ui.theme.CalculadoraDePropinaTheme
import com.epico.calculadoradepropina.ui.tip.TipScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraDePropinaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}