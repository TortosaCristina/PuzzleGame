package com.mcrt.puzzlegame.score

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoreDao {
    @Insert()
    fun insert(item: Score): Long
    @Delete
    fun delete(item: Score)
    @Update
    fun update(item: Score)
    @Query("SELECT * FROM score")
    fun getAll(): List<Score>
    @Query("SELECT * FROM score ORDER BY movimientos ASC")
    fun getAllMov(): List<Score>
    @Query("SELECT * FROM score ORDER BY id ASC")
    fun getAllNumPartida(): List<Score>
    @Query("SELECT * FROM score ORDER BY tiempo ASC")
    fun getAllTiempo(): List<Score>
}