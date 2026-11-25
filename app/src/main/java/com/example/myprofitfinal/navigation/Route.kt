package com.example.myprofitfinal.navigation

/**
 * Rutas de navegación de la app.
 *
 * Se usa un sealed interface para que el compilador nos ayude a no escribir
 * rutas "mágicas" repartidas por el código.
 */
sealed interface Route {
    val route: String

    data object Welcome : Route {
        override val route: String = "welcome"
    }

    data object Login : Route {
        override val route: String = "login"
    }

    data object Dashboard : Route {
        override val route: String = "dashboard"
    }

    data object Movimiento : Route {
        override val route: String = "movimiento"
    }

    data object Historial : Route {
        override val route: String = "historial"
    }

    data object Presupuestos : Route {
        override val route: String = "presupuestos"
    }

    data object Metas : Route {
        override val route: String = "metas"
    }

    data object Perfil : Route {
        override val route: String = "perfil"
    }
}
