package com.mcrt.puzzlegame.config

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.game.GameFragment
import java.io.IOException
import android.Manifest
import android.graphics.BitmapFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ConfigFragment : Fragment() {
    private lateinit var v: View
    private val REQUEST_IMAGE = 1001
    private var selectedBitmap: Bitmap? = null
    private lateinit var viewModel: ConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_config, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConfigViewModel::class.java)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dificultadSpinner = v.findViewById<Spinner>(R.id.difficultSpinner)
        val numFilasEdit = v.findViewById<EditText>(R.id.numFilas)
        val playGameButton = v.findViewById<Button>(R.id.playGameButton)
        val selectImagen = v.findViewById<Button>(R.id.seleccionarImagen)

        val dificultades = resources.getStringArray(R.array.dificultades)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dificultades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dificultadSpinner.adapter = adapter

        selectedBitmap = BitmapFactory.decodeResource(resources, R.drawable.error)
        dificultadSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (dificultades[position] == "Personalizado") {
                    v.findViewById<TextView>(R.id.numFilasText).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    v.findViewById<TextView>(R.id.numFilasText).setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.azul_marino_clarito
                        )
                    )
                    numFilasEdit.isEnabled = true
                    numFilasEdit.isFocusable = true
                    numFilasEdit.isFocusableInTouchMode = true
                    selectImagen.isEnabled = true
                } else {
                    v.findViewById<TextView>(R.id.numFilasText).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blanco_opacidad
                        )
                    )
                    v.findViewById<TextView>(R.id.numFilasText).setBackgroundColor(
                        Color.TRANSPARENT
                    )
                    numFilasEdit.isEnabled = false
                    numFilasEdit.isFocusable = false
                    numFilasEdit.isFocusableInTouchMode = false
                    selectImagen.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle default selection
            }
        }
        viewModel.numFilas.observe(viewLifecycleOwner, Observer { numFilas ->
            numFilasEdit.setText(numFilas.toString()) // Actualizar el EditText con el valor del LiveData
        })

        playGameButton.setOnClickListener {
            val dificultadSeleccionada = dificultadSpinner.selectedItem.toString()
            val numFilas = numFilasEdit.text.toString().toIntOrNull() ?: 0
            val fm = parentFragmentManager
            fm.commit {
                replace(
                    R.id.fragmentContainerView,
                    GameFragment.newInstance(dificultadSeleccionada, selectedBitmap!!, numFilas)
                )
                addToBackStack("replacement")
            }
        }

        selectImagen.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_IMAGE
                )
            } else {
                abrirGaleriaFotos()
            }
        }
    }

    private fun abrirGaleriaFotos() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        uri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConfigFragment()
    }
}