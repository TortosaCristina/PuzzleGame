package com.mcrt.puzzlegame.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigViewModel : ViewModel() {
    private val _numFilas = MutableLiveData<Int>()
    val numFilas: LiveData<Int> = _numFilas

    fun actualizarNumFilas(numero: Int) {
        _numFilas.value = numero
    }
}