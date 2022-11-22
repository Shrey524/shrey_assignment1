package com.example.myapplication.Network

import com.example.myapplication.Model.ApiData
import retrofit2.http.GET

interface RetroService {
    @GET("success_case")
    suspend fun successApi(): ApiData

    @GET("failure_case")
    suspend fun failureApi(): ApiData

}