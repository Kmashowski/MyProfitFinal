package com.example.myprofitfinal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myprofitfinal.model.Moneda
import com.example.myprofitfinal.model.Presupuesto
import com.example.myprofitfinal.ui.components.EmptyState
import com.example.myprofitfinal.ui.components.ScreenHeader
import com.example.myprofitfinal.ui.components.money
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

@Composable
fun PresupuestosScreen(navController: NavHostController, viewModel: MyProfitViewModel) {
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
    val monedaLabel = if (viewModel.tipoMoneda == Moneda.USD) "USD" else "COP"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(20.dp))
        ScreenHeader(
            title = "Presupuestos",
            subtitle = "Controla tus límites mensuales",
            onBack = { navController.popBackStack() },
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Nuevo presupuesto")
        }
        Spacer(Modifier.height(16.dp))

        if (viewModel.presupuestos.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.ShoppingCart,
                title = "Sin presupuestos",
                message = "Crea un presupuesto para controlar tus gastos por categoría.",
                actionLabel = "Nuevo presupuesto",
                onAction = { mostrarDialogo = true },
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
            ) {
                items(viewModel.presupuestos, key = { it.id }) { presupuesto ->
                    PresupuestoCard(
                        presupuesto = presupuesto,
                        tipoMoneda = viewModel.tipoMoneda,
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        NuevoPresupuestoDialog(
            monedaLabel = monedaLabel,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { categoria, limite ->
                viewModel.agregarPresupuesto(categoria, limite)
                mostrarDialogo = false
            },
        )
    }
}

@Composable
private fun PresupuestoCard(
    presupuesto: Presupuesto,
    tipoMoneda: Moneda = Moneda.USD,
) {
    val progreso = if (presupuesto.limite > 0) {
        (presupuesto.gastado / presupuesto.limite).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }
    val cercaDelLimite = progreso >= 0.9f

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = presupuesto.categoria,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "${(progreso * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (cercaDelLimite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth(),
                color = if (cercaDelLimite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${money(presupuesto.gastado, tipoMoneda)} gastados de ${money(presupuesto.limite, tipoMoneda)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NuevoPresupuestoDialog(
    monedaLabel: String,
    onDismiss: () -> Unit,
    onGuardar: (String, Double) -> Unit,
) {
    var categoria by rememberSaveable { mutableStateOf("") }
    var limite by rememberSaveable { mutableStateOf("") }
    val limiteValor = limite.toDoubleOrNull()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo presupuesto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = limite,
                    onValueChange = { nuevo ->
                        if (nuevo.isEmpty() || nuevo.matches(Regex("\\d*\\.?\\d*"))) {
                            limite = nuevo
                        }
                    },
                    label = { Text("Límite mensual ($monedaLabel)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = categoria.isNotBlank() && limiteValor != null && limiteValor > 0,
                onClick = { onGuardar(categoria.trim(), limiteValor ?: 0.0) },
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
