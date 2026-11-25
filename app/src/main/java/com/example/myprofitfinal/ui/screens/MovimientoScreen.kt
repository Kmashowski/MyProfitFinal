package com.example.myprofitfinal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myprofitfinal.model.Moneda
import com.example.myprofitfinal.model.TipoMovimiento
import com.example.myprofitfinal.navigation.Route
import com.example.myprofitfinal.ui.components.ScreenHeader
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientoScreen(navController: NavHostController, viewModel: MyProfitViewModel) {
    var tipo by rememberSaveable { mutableStateOf(TipoMovimiento.GASTO) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("Alimentación") }

    val categorias = listOf("Alimentación", "Transporte", "Vivienda", "Ocio", "Trabajo", "Otros")
    val montoValor = monto.toDoubleOrNull()
    val monedaLabel = if (viewModel.tipoMoneda == Moneda.USD) "USD" else "COP"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        ScreenHeader(
            title = "Registrar movimiento",
            subtitle = "Agrega un ingreso o gasto a tu historial",
        )
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(
                selected = tipo == TipoMovimiento.GASTO,
                onClick = { tipo = TipoMovimiento.GASTO },
                label = { Text("Gasto") },
                modifier = Modifier.weight(1f),
            )
            FilterChip(
                selected = tipo == TipoMovimiento.INGRESO,
                onClick = { tipo = TipoMovimiento.INGRESO },
                label = { Text("Ingreso") },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = monto,
            onValueChange = { nuevo ->
                if (nuevo.isEmpty() || nuevo.matches(Regex("\\d*\\.?\\d*"))) {
                    monto = nuevo
                }
            },
            label = { Text("Monto ($monedaLabel)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Categoría",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categorias) { cat ->
                FilterChip(
                    selected = cat == categoria,
                    onClick = { categoria = cat },
                    label = { Text(cat) },
                )
            }
        }

        Spacer(Modifier.height(28.dp))
        Button(
            onClick = {
                val valor = montoValor ?: return@Button
                viewModel.agregarMovimiento(
                    descripcion = descripcion.ifBlank { categoria },
                    categoria = categoria,
                    monto = valor,
                    tipo = tipo,
                )
                navController.navigate(Route.Historial.route) {
                    popUpTo(Route.Dashboard.route)
                    launchSingleTop = true
                }
            },
            enabled = montoValor != null && montoValor > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text("Guardar movimiento")
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = "El movimiento se agrega a la lista compartida de la demo (finanzas en memoria).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
