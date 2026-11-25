package com.example.myprofitfinal.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myprofitfinal.ui.screens.DashboardScreen
import com.example.myprofitfinal.ui.screens.HistorialScreen
import com.example.myprofitfinal.ui.screens.LoginScreen
import com.example.myprofitfinal.ui.screens.MetasScreen
import com.example.myprofitfinal.ui.screens.MovimientoScreen
import com.example.myprofitfinal.ui.screens.PerfilScreen
import com.example.myprofitfinal.ui.screens.PresupuestosScreen
import com.example.myprofitfinal.ui.screens.WelcomeScreen
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

/**
 * Destinos en los que se muestra la barra inferior.
 */
private val rutasConBottomBar = setOf(
    Route.Dashboard.route,
    Route.Movimiento.route,
    Route.Historial.route,
    Route.Metas.route,
    Route.Perfil.route,
)

@Composable
fun MyProfitNavHost(
    viewModel: MyProfitViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != null && rutasConBottomBar.contains(currentRoute)) {
                MyProfitBottomBar(navController = navController, currentRoute = currentRoute)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Welcome.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Route.Welcome.route) { WelcomeScreen(navController) }
            composable(Route.Login.route) { LoginScreen(navController, viewModel) }
            composable(Route.Dashboard.route) { DashboardScreen(navController, viewModel) }
            composable(Route.Movimiento.route) { MovimientoScreen(navController, viewModel) }
            composable(Route.Historial.route) { HistorialScreen(navController, viewModel) }
            composable(Route.Presupuestos.route) { PresupuestosScreen(navController, viewModel) }
            composable(Route.Metas.route) { MetasScreen(navController, viewModel) }
            composable(Route.Perfil.route) { PerfilScreen(navController, viewModel) }
        }
    }
}
