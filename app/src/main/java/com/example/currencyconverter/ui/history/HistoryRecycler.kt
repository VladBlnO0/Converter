package com.example.currencyconverter.ui.history

class HistoryRecycler(string: String, d: Double) {
    var currencyText: String? = null
    var valueText: Double? = null

    fun HistoryModel(currencyText: String?, valueText: Double?) {
        this.currencyText = currencyText
        this.valueText = valueText
    }

}