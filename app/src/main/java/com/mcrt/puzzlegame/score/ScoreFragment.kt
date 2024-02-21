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
        view?.findViewById<RecyclerView>(R.id.list)!!?.layoutManager = GridLayoutManager(context, 2)
        view?.findViewById<RecyclerView>(R.id.list)!!.adapter =
            this.viewModel.items.value?.let {
                ScoreRecyclerViewAdapter(it.toMutableList())
            }
        this.viewModel.items.observe(viewLifecycleOwner, {
            (view?.findViewById<RecyclerView>(R.id.list)!!.adapter as ScoreRecyclerViewAdapter).setValues(it)
            (view?.findViewById<RecyclerView>(R.id.list)!!.adapter as ScoreRecyclerViewAdapter).notifyDataSetChanged()
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ScoreFragment().apply {

                }
    }
}