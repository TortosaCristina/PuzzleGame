package com.mcrt.puzzlegame.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.score.ScoreViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class GameViewModel : ViewModel() {
    private lateinit var imagen: Bitmap
    private lateinit var piezas: MutableList<Bitmap>
    var movimientos = MutableLiveData<Int>()

    private lateinit var imagenOriginal: MutableList<Bitmap>
    //Carga las imagenes personalizadas
    fun cargarImagenPersonalizada(resources: Resources, bm:Bitmap, numFilas: Int) {
        imagen = bm
        val piezasDesordenadas = dividirImagen(imagen, numFilas,  numFilas)
        imagenOriginal = piezasDesordenadas.toMutableList()
        piezas = piezasDesordenadas.shuffled().toMutableList()
    }
    //Carga las imagenes de la api
    fun cargarImagen(resources: Resources,  bm:Bitmap, dificultad: String) {
        imagen =bm
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
    //Si esta resuelto no permite mover mas fichas
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
    //Obtenemos el tablero
    fun getTablero() : List<Bitmap> {
        return piezas
    }
    //Nos permite intercambiar las piezas
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
    //Incrementa los movimientos cada vez que se cambia una pieza
    fun incrementarMovimientos() {
        val movimientoActual = movimientos.value?: 0
        movimientos.value = movimientoActual + 1
    }
    //Divide la imagen para generar el tablero
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
    //Obtenemos los movimientos en un momento dado
    fun getMovimientos() : LiveData<Int> {
        return movimientos
    }
}