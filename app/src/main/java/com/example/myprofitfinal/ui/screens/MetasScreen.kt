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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.myprofitfinal.model.MetaAhorro
import com.example.myprofitfinal.ui.components.EmptyState
import com.example.myprofitfinal.ui.components.ScreenHeader
import com.example.myprofitfinal.ui.components.money
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

@Composable
fun MetasScreen(navController: NavHostController, viewModel: MyProfitViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
    ) {
        item {
            ScreenHeader(
                title = "Metas de ahorro",
                subtitle = "Ahorra con propósito",
            )
        }

        if (viewModel.metas.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Star,
                    title = "Sin metas todavía",
                    message = "Crea tu primera meta de ahorro para empezar a acumular.",
                )
            }
        } else {
            items(viewModel.metas, key = { it.id }) { meta ->
                MetaCard(
                    meta = meta,
                    tipoMoneda = viewModel.tipoMoneda,
                    onAportar = { viewModel.aportarMeta(meta.id, 100.0) },
                )
            }
        }
    }
}

@Composable
private fun MetaCard(
    meta: MetaAhorro,
    tipoMoneda: Moneda = Moneda.USD,
    onAportar: () -> Unit,
) {
    val progreso = if (meta.objetivo > 0) {
        (meta.ahorrado / meta.objetivo).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = meta.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "${(progreso * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${money(meta.ahorrado, tipoMoneda)} de ${money(meta.objetivo, tipoMoneda)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onAportar) {
                    Text("+ ${money(100.0, tipoMoneda)}")
                }
            }
            Text(
                text = "Meta para ${meta.fechaLimite}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
