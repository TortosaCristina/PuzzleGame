package com.mcrt.puzzlegame.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.config.ConfigFragment

class HomeFragment : Fragment() {
    private lateinit var v: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_home, container, false)
        var configButton = v.findViewById<Button>(R.id.configButton)
        configButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.commit {
                replace(R.id.fragmentContainerView, ConfigFragment.newInstance())
                addToBackStack("replacement")
            }
            true
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {}
    }
}
