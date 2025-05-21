package com.example.currencyconverter.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {

    private const val BASE_URL =
        "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/"

    val apiService: RetrofitInterface by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RetrofitInterface::class.java)
    }
}
