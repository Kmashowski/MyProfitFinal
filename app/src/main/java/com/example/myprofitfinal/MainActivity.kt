package com.example.myprofitfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myprofitfinal.data.AppDatabase
import com.example.myprofitfinal.data.AuthDao
import com.example.myprofitfinal.navigation.MyProfitNavHost
import com.example.myprofitfinal.ui.theme.MyProfitFinalTheme
import com.example.myprofitfinal.viewmodel.MyProfitViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Base de datos local para credenciales de acceso (Room).
        val db = AppDatabase.getInstance(this)
        val authDao = db.authDao()

        // ViewModel a nivel de Activity: el estado se comparte entre pantallas.
        val viewModel: MyProfitViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MyProfitViewModel(authDao) as T
                }
            },
        )[MyProfitViewModel::class.java]

        setContent {
            MyProfitFinalTheme(darkTheme = viewModel.modoOscuro) {
                MyProfitNavHost(viewModel = viewModel)
            }
        }
    }
}
