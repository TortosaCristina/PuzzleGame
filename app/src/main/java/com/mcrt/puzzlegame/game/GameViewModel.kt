package com.mcrt.puzzlegame.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private lateinit var imagen: Bitmap
    private lateinit var piezas: MutableList<Bitmap>
    fun cargarImagen(resources: Resources, resId: Int) {
        imagen = BitmapFactory.decodeResource(resources, resId)
        piezas = dividirImagen(imagen, 3, 3).shuffled().toMutableList()
    }
    fun getTablero() : List<Bitmap> {
        return piezas
    }
    fun intercambiarPiezas(origen: Int, destino: Int) {
        val temp = piezas[origen]
        piezas[origen] = piezas[destino]
        piezas[destino] = temp
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
}