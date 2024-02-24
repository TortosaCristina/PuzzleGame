package com.mcrt.puzzlegame.score

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    private lateinit var _context: Context
    lateinit var itemsRepository: ScoreRepository
    private lateinit var _items: MutableLiveData<MutableList<Score>>
    var selectedScore = Score()
    public val items: LiveData<MutableList<Score>>
        get() = _items
    fun init(c: Context) {
        this._context = c
        _items = MutableLiveData()
        this.itemsRepository = ScoreRepository(c)
        this._items.value = this.itemsRepository.getAll()
    }
    fun save(score: Score) {
        this._items.value?.add(score)
        this.itemsRepository.save(score)
        this.update()
    }
    private fun update() {
        var values = this._items.value
        this._items.value = values
    }
    public fun delete() {
        this._items.value?.remove(this.selectedScore)
        this.itemsRepository.delete(this.selectedScore)
        this.update()
    }
    fun orderByNumeroPartida() {
        _items.value = _items.value?.sortedBy { it.id }?.toMutableList()
    }

    fun orderByTiempo() {
        _items.value = _items.value?.sortedBy { it.tiempo }?.toMutableList()
    }

    fun orderByMovimientos() {
        _items.value = _items.value?.sortedBy { it.movimientos }?.toMutableList()
    }
}