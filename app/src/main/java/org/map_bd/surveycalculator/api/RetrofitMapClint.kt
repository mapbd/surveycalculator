package org.map_bd.surveycalculator.api

import org.map_bd.sotmasia2024.api.Gmapi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitMapClint {

    private const val BASE_URL = ""

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance = retrofit.create(Gmapi::class.java)
}