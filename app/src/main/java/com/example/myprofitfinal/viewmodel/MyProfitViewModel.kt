package com.example.myprofitfinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofitfinal.data.AuthDao
import com.example.myprofitfinal.data.AuthUser
import com.example.myprofitfinal.model.MetaAhorro
import com.example.myprofitfinal.model.Movimiento
import com.example.myprofitfinal.model.Moneda
import com.example.myprofitfinal.model.Presupuesto
import com.example.myprofitfinal.model.TipoMovimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Estado compartido de la demo.
 *
 * Se inicializa con datos de ejemplo para poder mostrar el flujo completo sin
 * backend. La autenticación de usuarios se guarda en una base de datos local
 * (Room).
 *
 * Al ser un ViewModel, sobrevive a cambios de configuración (rotación de pantalla).
 */
class MyProfitViewModel(
    private val authDao: AuthDao,
) : ViewModel() {

    // ===== Finanzas (datos de demostración en memoria) =====
    val movimientos = mutableStateListOf(
        Movimiento(1, "Salario mensual", "Trabajo", 2500.0, TipoMovimiento.INGRESO, "01 oct"),
        Movimiento(2, "Alquiler", "Vivienda", 650.0, TipoMovimiento.GASTO, "03 oct"),
        Movimiento(3, "Supermercado", "Alimentación", 180.5, TipoMovimiento.GASTO, "05 oct"),
        Movimiento(4, "Transporte público", "Transporte", 45.0, TipoMovimiento.GASTO, "07 oct"),
        Movimiento(5, "Proyecto freelance", "Trabajo", 320.0, TipoMovimiento.INGRESO, "10 oct"),
    )

    val presupuestos = mutableStateListOf(
        Presupuesto(1, "Alimentación", 400.0, 180.5),
        Presupuesto(2, "Transporte", 120.0, 45.0),
        Presupuesto(3, "Ocio", 150.0, 120.0),
    )

    val metas = mutableStateListOf(
        MetaAhorro(1, "Fondo de emergencia", 3000.0, 1200.0, "dic 2026"),
        MetaAhorro(2, "Viaje a la playa", 1500.0, 450.0, "jun 2027"),
        MetaAhorro(3, "Nuevo portátil", 1200.0, 900.0, "feb 2027"),
    )

    var tipoMoneda by mutableStateOf(Moneda.USD)
        private set

    var modoOscuro by mutableStateOf(false)
        private set

    val totalIngresos: Double
        get() = movimientos.filter { it.tipo == TipoMovimiento.INGRESO }.sumOf { it.monto }

    val totalGastos: Double
        get() = movimientos.filter { it.tipo == TipoMovimiento.GASTO }.sumOf { it.monto }

    val balance: Double
        get() = totalIngresos - totalGastos

    fun agregarMovimiento(descripcion: String, categoria: String, monto: Double, tipo: TipoMovimiento) {
        val nuevoId = (movimientos.maxOfOrNull { it.id } ?: 0) + 1
        movimientos.add(0, Movimiento(nuevoId, descripcion, categoria, monto, tipo, "Hoy"))
    }

    fun eliminarMovimiento(id: Int) {
        movimientos.removeAll { it.id == id }
    }

    fun agregarPresupuesto(categoria: String, limite: Double) {
        val nuevoId = (presupuestos.maxOfOrNull { it.id } ?: 0) + 1
        presupuestos.add(Presupuesto(nuevoId, categoria, limite, 0.0))
    }

    fun aportarMeta(id: Int, monto: Double) {
        val indice = metas.indexOfFirst { it.id == id }
        if (indice >= 0) {
            val meta = metas[indice]
            metas[indice] = meta.copy(ahorrado = (meta.ahorrado + monto).coerceAtMost(meta.objetivo))
        }
    }

    fun alternarModoOscuro() {
        modoOscuro = !modoOscuro
    }

    // ===== Moneda (USD / COP) =====
    fun cambiarMoneda() {
        tipoMoneda = if (tipoMoneda == Moneda.USD) Moneda.COP else Moneda.USD
    }

    // ===== Autenticación (base de datos local con Room) =====
    val usuariosAuth: StateFlow<List<AuthUser>> = authDao.obtenerUsuarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _usuarioActual = MutableStateFlow<AuthUser?>(null)
    val usuarioActual: StateFlow<AuthUser?> = _usuarioActual.asStateFlow()

    /** Valida credenciales contra la base de datos local. */
    suspend fun login(email: String, password: String): Boolean {
        val usuario = authDao.obtenerUsuarioPorEmail(email.trim())
        val exito = usuario != null && usuario.password == password
        if (exito) {
            _usuarioActual.value = usuario
        }
        return exito
    }

    /** Guarda una nueva cuenta en la base de datos local y la inicia. */
    suspend fun registrar(email: String, password: String): Boolean {
        val existe = authDao.obtenerUsuarioPorEmail(email.trim()) != null
        if (!existe) {
            authDao.insertarUsuario(AuthUser(email = email.trim(), password = password))
            val nuevo = authDao.obtenerUsuarioPorEmail(email.trim())
            _usuarioActual.value = nuevo
        }
        return !existe
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
    }
}
