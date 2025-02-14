package com.mcrt.puzzlegame.score

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.databinding.FragmentScoreItemBinding

class ScoreRecyclerViewAdapter (
    private var values: MutableList<Score>
) : RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentScoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idScore.text = item.id.toString()
        holder.movimientos.text = item.movimientos.toString()
        holder.tiempo.text = item.tiempo
        holder.imagenPuzzle.setImageBitmap(item.imagen)
        holder.dificultad.text = item.dificultad
    }
    inner class ViewHolder(binding: FragmentScoreItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idScore: TextView = binding.idPartida
        val movimientos: TextView = binding.numMovimientos
        val tiempo: TextView = binding.cantTiempo
        val imagenPuzzle: ImageView = binding.puzzleImageView
        val dificultad: TextView = binding.dificultad
    }
    public fun setValues(v: MutableList<Score>) {
        this.values = v
        this.notifyDataSetChanged()
    }
    override fun getItemCount(): Int = values.size
    fun sortByNumeroPartida() {
        values.sortBy { it.id }
        notifyDataSetChanged()
    }

    fun sortByTiempo() {
        values.sortBy { it.tiempo }
        notifyDataSetChanged()
    }

    fun sortByMovimientos() {
        values.sortBy { it.movimientos }
        notifyDataSetChanged()
    }
}