package com.example.myprofitfinal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myprofitfinal.model.Moneda
import com.example.myprofitfinal.navigation.Route
import com.example.myprofitfinal.ui.components.EmptyState
import com.example.myprofitfinal.ui.components.MovimientoItem
import com.example.myprofitfinal.ui.components.QuickActionCard
import com.example.myprofitfinal.ui.components.money
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

@Composable
fun DashboardScreen(navController: NavHostController, viewModel: MyProfitViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        Text(
            text = "Hola, Juan 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Este es tu resumen financiero",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Balance total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = money(viewModel.balance, viewModel.tipoMoneda),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Column {
                        Text(
                            text = "Ingresos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        )
                        Text(
                            text = money(viewModel.totalIngresos, viewModel.tipoMoneda),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    Column {
                        Text(
                            text = "Gastos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        )
                        Text(
                            text = money(viewModel.totalGastos, viewModel.tipoMoneda),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Acciones rápidas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                icon = Icons.Filled.Add,
                label = "Registrar",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Route.Movimiento.route) },
            )
            QuickActionCard(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Historial",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Route.Historial.route) },
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(
                icon = Icons.Filled.ShoppingCart,
                label = "Presupuestos",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Route.Presupuestos.route) },
            )
            QuickActionCard(
                icon = Icons.Filled.Star,
                label = "Metas",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Route.Metas.route) },
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Últimos movimientos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = { navController.navigate(Route.Historial.route) }) {
                Text("Ver todo")
            }
        }
        if (viewModel.movimientos.isEmpty()) {
            EmptyState(
                icon = Icons.AutoMirrored.Filled.List,
                title = "Sin movimientos",
                message = "Registra tu primer movimiento para verlo aquí.",
            )
        } else {
            viewModel.movimientos.take(3).forEach { movimiento ->
                MovimientoItem(movimiento = movimiento, tipoMoneda = viewModel.tipoMoneda)
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.cambiarMoneda() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Moneda",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = if (viewModel.tipoMoneda == Moneda.USD) "USD · Dólar estadounidense" else "COP · Peso colombiano",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
