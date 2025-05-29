package com.example.currencyconverter.db

class HistoryRepository(val dao: HistoryDao) {
    val allHistory = dao.getAllHistory()

    suspend fun insert(history: HistoryEntity) = dao.insert(history)
    suspend fun delete(history: HistoryEntity) = dao.delete(history)
}
