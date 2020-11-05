package com.example.myfirstofficeappecommerce

import com.example.myfirstofficeappecommerce.Models.MenuJson
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface{
    @GET("db")
  public suspend fun getRestData():MenuJson
}