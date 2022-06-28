package com.arthurbn.guessthenumber.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    val retrofitInstance: Retrofit

    init {
        retrofitInstance = Retrofit.Builder()
            .baseUrl("https://us-central1-ss-devops.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
    }

    fun getApiService(): ApiService {
        return retrofitInstance.create(ApiService::class.java)
    }

}