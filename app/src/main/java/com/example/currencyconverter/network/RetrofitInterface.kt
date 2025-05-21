package com.example.currencyconverter.network

import retrofit2.http.GET
import com.example.currencyconverter.ui.home.CurrencyModel
import com.example.currencyconverter.ui.home.CurrencyNamesModel

interface RetrofitInterface {
    @GET("currencies/eur.json")
    suspend fun getRates(): CurrencyModel

    @GET("currencies.json")
    suspend fun getCurrencyNames(): Map<String, String>
}