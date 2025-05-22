package com.example.currencyconverter.ui.history

data class HistoryModel (
        val fromCurrencyCode: String,
        val toCurrencyCode: String,

        val valueText: String,
        val convertedValueText: String
)