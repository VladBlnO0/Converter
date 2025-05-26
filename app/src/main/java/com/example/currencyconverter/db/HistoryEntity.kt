package com.example.currencyconverter.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")

data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val inputValue: String,
    val resultValue: String
)
