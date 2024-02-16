package com.mcrt.puzzlegame.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        var playGameButton = v.findViewById<Button>(R.id.playGameButton)
        playGameButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.commit {
                replace(R.id.fragmentContainerView, GameFragment.newInstance())
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