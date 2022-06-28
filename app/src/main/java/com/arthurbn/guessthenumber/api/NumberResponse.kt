package com.arthurbn.guessthenumber.api

import com.google.gson.annotations.SerializedName

data class NumberResponse(
    @SerializedName("value")
    val number: Int
)