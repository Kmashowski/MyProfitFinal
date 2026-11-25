package com.example.myprofitfinal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myprofitfinal.model.TipoMovimiento
import com.example.myprofitfinal.navigation.Route
import com.example.myprofitfinal.ui.components.EmptyState
import com.example.myprofitfinal.ui.components.MovimientoItem
import com.example.myprofitfinal.ui.components.ScreenHeader
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(navController: NavHostController, viewModel: MyProfitViewModel) {
    var filtro by rememberSaveable { mutableStateOf("Todos") }

    val movimientos = when (filtro) {
        "Ingresos" -> viewModel.movimientos.filter { it.tipo == TipoMovimiento.INGRESO }
        "Gastos" -> viewModel.movimientos.filter { it.tipo == TipoMovimiento.GASTO }
        else -> viewModel.movimientos.toList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(20.dp))
        ScreenHeader(
            title = "Historial",
            subtitle = "Todos tus movimientos registrados",
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Todos", "Ingresos", "Gastos").forEach { opcion ->
                FilterChip(
                    selected = filtro == opcion,
                    onClick = { filtro = opcion },
                    label = { Text(opcion) },
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        if (movimientos.isEmpty()) {
            EmptyState(
                icon = Icons.AutoMirrored.Filled.List,
                title = "Sin movimientos",
                message = "Registra tu primer ingreso o gasto para verlo aquí.",
                actionLabel = "Registrar movimiento",
                onAction = { navController.navigate(Route.Movimiento.route) },
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
            ) {
                items(movimientos, key = { it.id }) { movimiento ->
                    MovimientoItem(
                        movimiento = movimiento,
                        tipoMoneda = viewModel.tipoMoneda,
                        onDelete = { viewModel.eliminarMovimiento(movimiento.id) },
                    )
                }
            }
        }
    }
}
