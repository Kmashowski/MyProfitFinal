package com.example.myprofitfinal.model

/**
 * Modelos de datos de demostración para MyProfit.
 * La autenticación de usuarios se persiste en una base de datos local (Room);
 * las finanzas de la demo viven en memoria.
 */

enum class TipoMovimiento { INGRESO, GASTO }

enum class Moneda { USD, COP }

data class Movimiento(
    val id: Int,
    val descripcion: String,
    val categoria: String,
    val monto: Double,
    val tipo: TipoMovimiento,
    val fecha: String,
)

data class Presupuesto(
    val id: Int,
    val categoria: String,
    val limite: Double,
    val gastado: Double,
)

data class MetaAhorro(
    val id: Int,
    val nombre: String,
    val objetivo: Double,
    val ahorrado: Double,
    val fechaLimite: String,
)
