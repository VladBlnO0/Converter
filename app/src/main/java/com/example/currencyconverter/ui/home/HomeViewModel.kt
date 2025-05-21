package com.example.currencyconverter.ui.home
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.network.RetrofitClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    val currencyList = MutableLiveData<List<String>>()

    fun loadRates() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRates()
                currencyList.value = response.eur.keys.map { it.uppercase() }.sorted()
            } catch (e: Exception) {
                e.printStackTrace()
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
