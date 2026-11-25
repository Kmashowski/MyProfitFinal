package com.example.myprofitfinal.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

private data class DestinoBottomBar(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * Barra inferior con los destinos principales de la app.
 * Presupuestos e Historial técnicos (detalle) se abren desde el Dashboard,
 * pero Historial también aparece aquí para acceso rápido.
 */
@Composable
fun MyProfitBottomBar(navController: NavHostController, currentRoute: String?) {
    val destinos = listOf(
        DestinoBottomBar(Route.Dashboard.route, "Inicio", Icons.Filled.Home),
        DestinoBottomBar(Route.Movimiento.route, "Registrar", Icons.Filled.AddCircle),
        DestinoBottomBar(Route.Historial.route, "Historial", Icons.AutoMirrored.Filled.List),
        DestinoBottomBar(Route.Metas.route, "Metas", Icons.Filled.Star),
        DestinoBottomBar(Route.Perfil.route, "Perfil", Icons.Filled.Person),
    )

    NavigationBar {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = currentRoute == destino.route,
                onClick = {
                    if (currentRoute != destino.route) {
                        navController.navigate(destino.route) {
                            // Mantiene el Dashboard como raíz del flujo financiero.
                            popUpTo(Route.Dashboard.route)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(destino.icon, contentDescription = destino.label) },
                label = { Text(destino.label) },
            )
        }
    }
}
