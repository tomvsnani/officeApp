package com.example.myfirstofficeappecommerce

import android.app.Application
import android.util.Log
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.google.android.material.tabs.TabLayout

class ApplicationClass : Application() {


    companion object {
        var selectedItemsList: List<CategoriesModelClass>? = ArrayList()
        var selectedTab:TabLayout.Tab?=null
        var selectedVariantList:MutableList<VariantsModelClass>?=ArrayList()

    }


    override fun onCreate() {
        super.onCreate()
        CategoriesDataProvider.getAllTheCollections(applicationContext)
        //CategoriesDataProvider.getRemoteData(applicationContext)
    }


}