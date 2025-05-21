package com.example.currencyconverter.ui.home
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.network.RetrofitClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.example.currencyconverter.ui.home.CurrencyModel
import com.example.currencyconverter.ui.home.CurrencyNamesModel

class HomeViewModel : ViewModel() {
    val currencyRates = MutableLiveData<Map<String, Double>>()

    fun loadRates(base: String = "eur") {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRates(base)
                currencyRates.value = response.eur
            } catch (e: Exception) {
                Log.e("Rates", "Failed to load rates", e)
            }
        }
    }

    val currencyNames = MutableLiveData<Map<String, String>>()

    fun loadCurrencyNames() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCurrencyNames()
                currencyNames.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
