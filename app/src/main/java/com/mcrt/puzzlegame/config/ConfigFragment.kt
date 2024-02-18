package com.mcrt.puzzlegame.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.game.GameFragment

class ConfigFragment : Fragment() {
    private lateinit var v: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_config, container, false)
        val dificultadSpinner = v.findViewById<Spinner>(R.id.difficultSpinner)

        val dificultades = resources.getStringArray(R.array.dificultades)

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dificultades)

        // Especificar el layout para los elementos desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Vincular el adaptador al Spinner
        dificultadSpinner.adapter = adapter

        var playGameButton = v.findViewById<Button>(R.id.playGameButton)
        playGameButton.setOnClickListener {
            val dificultadSeleccionada = dificultadSpinner.selectedItem.toString()
            val fm: FragmentManager = parentFragmentManager
            fm.commit {
                replace(R.id.fragmentContainerView, GameFragment.newInstance(dificultadSeleccionada))
                addToBackStack("replacement")
            }
            true
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConfigFragment().apply {}
    }
}