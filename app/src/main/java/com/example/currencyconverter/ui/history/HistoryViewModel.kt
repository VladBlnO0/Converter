package com.example.currencyconverter.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    data class HistoryViewModel(
        val currencyText: String,
        val valueText: Double
    )

    private val _historyList = MutableLiveData<MutableList<HistoryRecycler>>(mutableListOf())
    val historyList: LiveData<MutableList<HistoryRecycler>> get() = _historyList

    fun addHistory(item: HistoryRecycler) {
        val currentList = _historyList.value ?: mutableListOf()
        currentList.add(item)
        _historyList.value = currentList
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is History Fragment"
//    }
//    val text: LiveData<String> = _text
}