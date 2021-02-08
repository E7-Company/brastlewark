package com.axa.brastlewark.data.remote

import com.axa.brastlewark.model.Brastlewark
import retrofit2.Response
import retrofit2.http.GET

interface BrastlewarkAPI {

    @GET("data.json")
    suspend fun getBrastlewark(): Response<Brastlewark>

}