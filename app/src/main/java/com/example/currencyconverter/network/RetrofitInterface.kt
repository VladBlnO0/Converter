package com.example.currencyconverter.network

import retrofit2.http.GET
import retrofit2.http.Path

import com.example.currencyconverter.ui.home.CurrencyModel

interface RetrofitInterface {
    @GET("currencies/{base}.json")
    suspend fun getRates(@Path("base") base: String): CurrencyModel

    @GET("currencies.json")
    suspend fun getCurrencyNames(): Map<String, String>
}