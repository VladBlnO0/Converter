package com.example.currencyconverter.network;

import retrofit2.http.GET;
import com.example.currencyconverter.ui.home.CurrencyModel;

interface RetrofitInterface {
    @GET("eur.json")
    suspend fun getRates(): CurrencyModel
}