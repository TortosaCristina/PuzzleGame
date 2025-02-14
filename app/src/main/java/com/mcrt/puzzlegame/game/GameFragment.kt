package com.mcrt.puzzlegame.game

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcrt.puzzlegame.AppDatabase
import com.mcrt.puzzlegame.MainActivity
import com.mcrt.puzzlegame.R
import com.mcrt.puzzlegame.score.Score
import com.mcrt.puzzlegame.score.ScoreDao
import com.mcrt.puzzlegame.score.ScoreViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var adapter: GameAdapter
    private lateinit var v: View
    private lateinit var movimientosTextView: TextView
    private var numColumnas = 0
    private lateinit var imageView: ImageView
    private val scoresViewModel: ScoreViewModel by activityViewModels<ScoreViewModel>()


    private var tiempoInicio: Long = 0 //Tiempo de inicio del cronometro
    private var tiempoTranscurrido: Job? = null
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
        //Cargamos los spinners de progreso mientras hacemos la peticion
        v.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.VISIBLE
        v.findViewById<ProgressBar>(R.id.puzzleProgress).visibility = View.VISIBLE

        var imageUrl = "https://cataas.com/cat?type=square"
        //Cogemos los argumentos que hemos pasado del fragmento Config
        val dificultad = arguments?.getString(ARG_DIFICULTAD)
        val imagenPersonalizada: Bitmap? = arguments?.getParcelable(ARG_IMAGEN)
        val numFilas = arguments?.getInt(ARG_FILAS_CUSTOM)
        //Si la dificultad es personalizda le pasaremos imagen y columnas personalizadas
        //Sino, tendran valores por defecto y la imagen sera una aleatoria de la api
        if (dificultad == "Personalizado") {
            imageView = v.findViewById<ImageView>(R.id.puzzleImagen)
            imageView.setImageBitmap(imagenPersonalizada!!)
            dificultad?.let {
                numColumnas = when (dificultad) {
                    "Fácil" -> 3
                    "Intermedio" -> 4
                    "Difícil" -> 5
                    "Personalizado" -> numFilas!!
                    else -> 3 }
                viewModel.cargarImagenPersonalizada(resources, imagenPersonalizada!! , numColumnas)
                v.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.GONE
                v.findViewById<ProgressBar>(R.id.puzzleProgress).visibility = View.GONE
            }
            val piezas = viewModel.getTablero()
            val recyclerView = v.findViewById<RecyclerView>(R.id.puzzleRecycletView)
            recyclerView.layoutManager = GridLayoutManager(context, numColumnas!!)
            adapter = GameAdapter(piezas, viewModel, numColumnas!!)
            recyclerView.adapter = adapter
            movimientosTextView = v.findViewById<TextView>(R.id.movimientosText)
            iniciarCronometro()
        } else {
            imageView = v.findViewById<ImageView>(R.id.puzzleImagen)
            Picasso.get()
                .load(imageUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) //No se almacena en la cache para que me saque diferentes imagenes
                .error(R.drawable.imagen_prueba) // Imagen de error para mostrar en caso de fallo
                .into(imageView, object : Callback {
                    override fun onSuccess() {

                        dificultad?.let {
                            //viewModel.cargarImagen(resources, R.drawable.imagen_prueba, it)
                            viewModel.cargarImagen(resources,  imageView.drawable.toBitmap(), it)
                            v.findViewById<ProgressBar>(R.id.imageProgress).visibility = View.GONE
                            v.findViewById<ProgressBar>(R.id.puzzleProgress).visibility = View.GONE
                        }
                        val piezas = viewModel.getTablero()
                        val recyclerView = v.findViewById<RecyclerView>(R.id.puzzleRecycletView)
                        numColumnas = when (dificultad) {
                            "Fácil" -> 3
                            "Medio" -> 4
                            "Difícil" -> 5
                            else -> 3 }
                        recyclerView.layoutManager = GridLayoutManager(context, numColumnas)
                        adapter = GameAdapter(piezas, viewModel, numColumnas)
                        recyclerView.adapter = adapter
                        movimientosTextView = v.findViewById<TextView>(R.id.movimientosText)
                        iniciarCronometro()
                    }
                    override fun onError(e: Exception?) {
                        // Ocurrió un error al cargar la imagen
                        Log.e("Picasso", "Error loading image", e)
                    }
                })
        }
        return v
    }
    //Inicia el cronometro
    private fun iniciarCronometro() {
        tiempoInicio = System.currentTimeMillis()
        tiempoTranscurrido = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(1000)
                val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio
                actualizarCronometro(tiempoTranscurrido)
            }
        }
    }
    //Lo actualiza para que se vea en la pantalla
    private fun actualizarCronometro(tiempoTranscurrido: Long) {
        //Convertir el tiempo en horas, minutos y segundos
        val segundos = (tiempoTranscurrido / 1000).toInt()
        val horas = segundos / 3600
        val minutos = (segundos % 3600) / 60
        val segundosRestantes = segundos % 60
        //Formatear el tiempo en formato HH:MM:SS
        val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes)
        v.findViewById<TextView>(R.id.cronometro).text = tiempoFormateado
    }
    override fun onDestroyView() {
        super.onDestroyView()
        tiempoTranscurrido?.cancel() // Detener el cronómetro cuando el Fragmento se destruye
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovimientos().observe(viewLifecycleOwner) { movimientos ->
            movimientosTextView.text = movimientos.toString()
            if (viewModel.isResuelto(numColumnas)) {
                tiempoTranscurrido?.cancel() // Detener el cronómetro
                // Aquí puedes mostrar un mensaje de felicitaciones, etc.
                val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio
                val segundos = (tiempoTranscurrido / 1000).toInt()
                val horas = segundos / 3600
                val minutos = (segundos % 3600) / 60
                val segundosRestantes = segundos % 60
                //Formatear el tiempo en formato HH:MM:SS
                val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes)
                val dificultad = arguments?.getString(ARG_DIFICULTAD)
                val database = context?.let { AppDatabase.getInstance(it) }
                //Almacena la partida cuando hemos acabado la partida
                var score = Score(imageView.drawable.toBitmap(), movimientos, tiempoFormateado, dificultad!!)
                score.id = database?.scoreDao()?.insert(score)
                this.scoresViewModel.save(score)
                mostrarAlertaPuzzleResuelto(tiempoTranscurrido, movimientos)
            }
        }
    }
    //Alerta que saldra cuado terminemos el puzzle, concretamente un segundo despues
    private fun mostrarAlertaPuzzleResuelto(tiempoTranscurrido: Long, numMovimientos: Int) {
        val segundos = (tiempoTranscurrido / 1000).toInt()
        val horas = segundos / 3600
        val minutos = (segundos % 3600) / 60
        val segundosRestantes = segundos % 60
        // Formatear el tiempo en formato HH:MM:SS
        val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes)

        val mensaje = "¡Puzzle resuelto!\n\nTiempo: $tiempoFormateado\nMovimientos: $numMovimientos"

        // Se lanza una corrutina
        GlobalScope.launch {
            // Se suspende la corrutina durante un segundo
            delay(1000)
            // Se utiliza el contexto del hilo principal para mostrar la alerta
            withContext(Dispatchers.Main) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("¡Felicidades!")
                    .setMessage(mensaje)
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        val fm: FragmentManager = parentFragmentManager
                        fm.popBackStack()
                        fm.popBackStack()
                        dialog.dismiss()
                    }
                    .setCancelable(false) // Evita que el usuario pueda cerrar la alerta al hacer clic fuera de ella

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }
    companion object {
        private const val ARG_DIFICULTAD = "dificultad"
        private const val ARG_FILAS_CUSTOM = "numFilas"
        private const val ARG_IMAGEN = "imagen"
        @JvmStatic
        fun newInstance(dificultad: String, imagen: Bitmap, numFilas: Int) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DIFICULTAD, dificultad)
                    putParcelable(ARG_IMAGEN, imagen)
                    putInt(ARG_FILAS_CUSTOM, numFilas)
                }
            }
    }
}