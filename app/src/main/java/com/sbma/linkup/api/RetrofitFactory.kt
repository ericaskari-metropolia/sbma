package com.sbma.linkup.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {
    fun makeApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://sbma.ericaskari.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }
}