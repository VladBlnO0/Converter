package com.example.currencyconverter.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    private val _historyList = MutableLiveData<MutableList<HistoryModel>>(mutableListOf())
    val historyList: LiveData<MutableList<HistoryModel>> get() = _historyList
    val selectedItem = MutableLiveData<HistoryModel?>()
    fun selectItem(item: HistoryModel?) {
        selectedItem.value = item
    }
    fun deleteItem(item: HistoryModel?) {
        val currentList = _historyList.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(item)
            _historyList.value = updatedList
        }
    }

    fun addHistory(item: HistoryModel, onDuplicate: () -> Unit) {
        val currentList = _historyList.value ?: mutableListOf()

        if (currentList.any {
            it.fromCurrencyCode == item.fromCurrencyCode &&
                    it.toCurrencyCode == item.toCurrencyCode &&
                    it.valueText == item.valueText &&
                    it.convertedValueText == item.convertedValueText
            })
        {
            onDuplicate.invoke()
            return
        }

        currentList.add(item)
        _historyList.value = currentList
    }

}