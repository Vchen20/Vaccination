package com.example.vaccination


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Covid19Service {
    @GET("vaccine/coverage/countries")
    fun getVaccinations(@Query("lastdays") lastdays : Int) : Call<List<VaccinationInfo>>

    @GET("all")
    fun getWorldWideCases() : Call<List<VaccinationInfo>>
}