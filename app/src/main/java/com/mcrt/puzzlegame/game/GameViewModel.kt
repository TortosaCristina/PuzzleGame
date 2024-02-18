package com.mcrt.puzzlegame.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private lateinit var imagen: Bitmap
    private lateinit var piezas: MutableList<Bitmap>
    private var movimientos = MutableLiveData<Int>()

    private lateinit var imagenOriginal: MutableList<Bitmap>
    fun cargarImagen(resources: Resources, resId: Int, dificultad: String) {
        imagen = BitmapFactory.decodeResource(resources, resId)
        val filasColumnas = when (dificultad) {
            "Fácil" -> 3
            "Intermedio" -> 4
            "Difícil" -> 5
            else -> throw IllegalArgumentException("Dificultad no válida")
        }
        val piezasDesordenadas = dividirImagen(imagen, filasColumnas, filasColumnas)
        imagenOriginal = piezasDesordenadas.toMutableList()
        piezas = piezasDesordenadas.shuffled().toMutableList()
    }
    fun isResuelto(dimensiones: Int): Boolean {
        for ((index, pieza) in piezas.withIndex()) {
            val posicionCorrecta = index % (dimensiones * dimensiones) // 9 es el tamaño del rompecabezas
            val posicionActual = piezas.indexOf(imagenOriginal[posicionCorrecta])
            if (index != posicionActual) {
                return false
            }
        }
        return true
    }
    fun getTablero() : List<Bitmap> {
        return piezas
    }
    fun intercambiarPiezas(origen: Int, destino: Int, dimensiones: Int) {
        if (!isResuelto(dimensiones)) {
            if (origen != destino) {
                val temp = piezas[origen]
                piezas[origen] = piezas[destino]
                piezas[destino] = temp
                incrementarMovimientos()
            }
        }
    }
    fun incrementarMovimientos() {
        val movimientoActual = movimientos.value?: 0
        movimientos.value = movimientoActual + 1
    }
    private fun dividirImagen(imagen: Bitmap, filas: Int, columnas: Int) : List<Bitmap> {
        val anchoPieza = imagen.width / columnas
        val largoPieza = imagen.height / filas
        val piezas = mutableListOf<Bitmap>()
        for (y in 0 until filas) {
            for (x in 0 until columnas) {
                val pieza = Bitmap.createBitmap(imagen,x * anchoPieza, y * largoPieza, anchoPieza, largoPieza)
                piezas.add(pieza)
            }
        }
        return piezas
    }
    fun getMovimientos() : LiveData<Int> {
        return movimientos
    }
}