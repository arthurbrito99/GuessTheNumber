package com.arthurbn.guessthenumber.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("rand")
    suspend fun getNumber(@Query("min") min: Int, @Query("max") max: Int): NumberResponse
}