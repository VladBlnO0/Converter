package com.example.currencyconverter.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Delete
    suspend fun delete(history: HistoryEntity)

    @Query("DELETE FROM history_table")
    suspend fun deleteAll()
}
