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
    }
    inner class ViewHolder(binding: FragmentScoreItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idScore: TextView = binding.idPartida
        val movimientos: TextView = binding.numMovimientos
        val tiempo: TextView = binding.cantTiempo
        val imagenPuzzle: ImageView = binding.puzzleImageView
    }
    public fun setValues(v: MutableList<Score>) {
        this.values = v
        this.notifyDataSetChanged()
    }
    override fun getItemCount(): Int = values.size
}