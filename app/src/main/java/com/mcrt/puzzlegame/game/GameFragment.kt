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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter
    private lateinit var v: View
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
        viewModel.cargarImagen(resources, R.drawable.imagen_prueba)
        val piezas = viewModel.getTablero()
        val recyclerView = v.findViewById<RecyclerView>(R.id.puzzleRecycletView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = GameAdapter(piezas, viewModel)
        recyclerView.adapter = adapter
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameFragment().apply {

            }
    }
}