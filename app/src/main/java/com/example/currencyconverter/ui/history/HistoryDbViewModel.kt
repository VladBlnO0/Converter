package com.example.currencyconverter.ui.history

import android.app.Application
import androidx.lifecycle.*
import com.example.currencyconverter.db.AppDatabase
import com.example.currencyconverter.db.HistoryEntity
import com.example.currencyconverter.db.HistoryRepository
import kotlinx.coroutines.launch

class HistoryDbViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allHistory: LiveData<List<HistoryEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun insert(history: HistoryEntity) = viewModelScope.launch {
        repository.insert(history)
    }

    fun delete(history: HistoryEntity) = viewModelScope.launch {
        repository.delete(history)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
