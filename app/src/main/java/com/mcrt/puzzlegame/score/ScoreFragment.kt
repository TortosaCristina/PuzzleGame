package com.mcrt.puzzlegame.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.score.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class ScoreFragment : Fragment() {

    private val viewModel: ScoreViewModel by activityViewModels<ScoreViewModel>()
    private var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_score_list, container, false)
        setupRecyclerView() //Seleccionamos el adaptador
        setupFloatingActionButtons() // Aquí se llama al método para configurar los botones flotantes
        observeViewModel() //Observamos los cambios
        return view
    }

    private fun setupRecyclerView() {
        view?.findViewById<RecyclerView>(R.id.list)?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ScoreRecyclerViewAdapter(viewModel.items.value ?: mutableListOf())
        }
    }

    private fun setupFloatingActionButtons() {
        view?.findViewById<FloatingActionButton>(R.id.orderNumPartida)?.setOnClickListener {
            viewModel.orderByNumeroPartida()
        }
        view?.findViewById<FloatingActionButton>(R.id.orderTiempo)?.setOnClickListener {
            viewModel.orderByTiempo()
        }
        view?.findViewById<FloatingActionButton>(R.id.orderMovimientos)?.setOnClickListener {
            viewModel.orderByMovimientos()
        }
    }

    private fun observeViewModel() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            (view?.findViewById<RecyclerView>(R.id.list)?.adapter as? ScoreRecyclerViewAdapter)?.apply {
                setValues(items)
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ScoreFragment().apply {

                }
    }
}