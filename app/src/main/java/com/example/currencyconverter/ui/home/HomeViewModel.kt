package com.example.currencyconverter.ui.home
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.network.RetrofitClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val currencyRates = MutableLiveData<Map<String, Double>>()
    val currencyNames = MutableLiveData<Map<String, String>>()
    val selectedFromCurrency = MutableLiveData<String>("USD")
    val selectedToCurrency = MutableLiveData<String>("UAH")

    fun loadRates(base: String = "eur") {
        if (currencyRates.value != null && currencyRates.value!!.isNotEmpty()) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRates(base)
                currencyRates.value = response.eur
            } catch (e: Exception) {
                Log.e("Rates", "Failed to load rates", e)
            }
        }
    }


    fun loadCurrencyNames() {
        if (currencyNames.value != null && currencyNames.value!!.isNotEmpty()) return

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
