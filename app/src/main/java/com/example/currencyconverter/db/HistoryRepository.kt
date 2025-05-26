package com.example.currencyconverter.db

class HistoryRepository(private val dao: HistoryDao) {
    val allHistory = dao.getAllHistory()

    suspend fun insert(history: HistoryEntity) = dao.insert(history)
    suspend fun delete(history: HistoryEntity) = dao.delete(history)
    suspend fun deleteAll() = dao.deleteAll()
}
