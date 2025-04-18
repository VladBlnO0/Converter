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

    fun addHistory(item: HistoryModel, onDuplicate: () -> Unit) {
        val currentList = _historyList.value ?: mutableListOf()

        if (currentList.any {
            it.currencyText == item.currencyText &&
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

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is History Fragment"
//    }
//    val text: LiveData<String> = _text
}