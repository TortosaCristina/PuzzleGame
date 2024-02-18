package com.mcrt.puzzlegame.game

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.R

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter
    private lateinit var v: View
    private lateinit var movimientosTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_game, container, false)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        v.findViewById<ImageView>(R.id.puzzleImagen).setImageResource(R.drawable.imagen_prueba)

        val dificultad = arguments?.getString(ARG_DIFICULTAD)

        dificultad?.let {
            viewModel.cargarImagen(resources, R.drawable.imagen_prueba, it)
        }
        val piezas = viewModel.getTablero()
        val recyclerView = v.findViewById<RecyclerView>(R.id.puzzleRecycletView)
        val numColumnas = when (dificultad) {
            "Fácil" -> 3
            "Intermedio" -> 4
            "Difícil" -> 5
            else -> 3
        }
        recyclerView.layoutManager = GridLayoutManager(context, numColumnas)
        adapter = GameAdapter(piezas, viewModel, numColumnas)
        recyclerView.adapter = adapter
        movimientosTextView = v.findViewById<TextView>(R.id.movimientosText)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovimientos().observe(viewLifecycleOwner) { movimientos ->
            movimientosTextView.text = movimientos.toString()
        }
    }
    companion object {
        private const val ARG_DIFICULTAD = "dificultad"
        @JvmStatic
        fun newInstance(dificultad: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DIFICULTAD, dificultad)
                }
            }
    }
}