package com.example.myfirstofficeappecommerce

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface{
    @GET("menu_new.json")
  public  fun getRestData():Call<MenuJson>
}