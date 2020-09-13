package com.example.myfirstofficeappecommerce

import android.app.Application
import android.util.Log
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass

class ApplicationClass : Application() {


    companion object {
         var selectedItemsList: List<CategoriesModelClass>? = ArrayList()

    }


    override fun onCreate() {
        super.onCreate()
   CategoriesDataProvider.getRemoteData(applicationContext)
    }


}