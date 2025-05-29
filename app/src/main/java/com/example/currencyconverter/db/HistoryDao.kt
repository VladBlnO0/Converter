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

    @Query("SELECT COUNT(*) FROM history_table WHERE fromCurrency = :from AND toCurrency = :to AND inputValue = :input AND resultValue = :result")
    suspend fun exists(from: String, to: String, input: String, result: String): Int

}
