package com.mcrt.puzzlegame.score

import android.content.Context
import com.mcrt.puzzlegame.AppDatabase

class ScoreRepository(var context: Context) {
    private var _scoreDao: ScoreDao
    init {
        val database = AppDatabase.getInstance(context)
        _scoreDao = database.scoreDao()
    }
    fun delete(item: Score) {
        this._scoreDao.delete(item)
    }
    fun insert(item: Score): Long {
        return this._scoreDao.insert(item)
    }
    fun getAll(): MutableList<Score> {
        return _scoreDao.getAll().toMutableList()
    }
    fun getAllMov(): MutableList<Score> {
        return _scoreDao.getAllMov().toMutableList()
    }
    fun getAllNumPartida(): MutableList<Score> {
        return _scoreDao.getAllNumPartida().toMutableList()
    }
    fun getAllTiempo(): MutableList<Score> {
        return _scoreDao.getAllTiempo().toMutableList()
    }
    fun update(item: Score) {
        this._scoreDao.update(item)
    }
    fun save(selected: Score): Long? {
        if (selected.id == null) {
            selected.id = this._scoreDao.insert(selected)
            return selected.id
        } else {
            this._scoreDao.update(selected)
            return selected.id
        }
    }
}