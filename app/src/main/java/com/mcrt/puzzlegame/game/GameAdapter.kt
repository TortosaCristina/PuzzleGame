package com.mcrt.puzzlegame.game

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.R

class GameAdapter(private val piezas: List<Bitmap>, private val viewModel: GameViewModel) :
    RecyclerView.Adapter<GameAdapter.PiezaViewHolder>(){
    private var selectedPosition: Int? = null
        inner class PiezaViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            val imagenView: ImageView = itemView.findViewById(R.id.imageView)
            init {
                imagenView.setOnClickListener {
                    val posicionActual = adapterPosition
                    if (selectedPosition == null) {
                        selectedPosition = posicionActual
                    } else {
                        viewModel.intercambiarPiezas(selectedPosition!!, posicionActual)
                        notifyItemChanged(selectedPosition!!)
                        notifyItemChanged(posicionActual)
                        selectedPosition = null
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiezaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.piezas_puzzle, parent, false)
        return PiezaViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: PiezaViewHolder, position: Int) {
        holder.imagenView.setImageBitmap(piezas[position])
        if (position == selectedPosition) {
            holder.imagenView.alpha = 0.5f // Cambia la opacidad de la imagen seleccionada
        } else {
            holder.imagenView.alpha = 1.0f // Restablece la opacidad de otras imágenes
        }
    }
    override fun getItemCount(): Int {
        return piezas.size
    }
}