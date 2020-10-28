package com.example.myfirstofficeappecommerce

import android.app.Activity
import com.example.myfirstofficeappecommerce.Database.MyDatabase
import android.app.Application
import android.util.Log
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.google.android.material.tabs.TabLayout

class ApplicationClass : Application() {


    companion object {
        var menucategorylist: List<CategoriesModelClass> = ArrayList()
        var selectedTab: Int? = null
        var selectedVariantList: MutableList<VariantsModelClass>? = ArrayList()
        var recentsList: MutableList<VariantsModelClass>? = ArrayList()
        var mydb: MyDatabase? = null
        var weburl: String? = null
        var signInType: String = ""
        var checkoutId: String = ""
    }


    override fun onCreate() {
        super.onCreate()
        CategoriesDataProvider.getAllTheCollections(applicationContext)
        mydb = MyDatabase.getDbInstance(application = applicationContext)

        //CategoriesDataProvider.getRemoteData(applicationContext)

    }

    fun getCustomerToken(activity: MainActivity): String {
        return activity.getPreferences(Activity.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_TOKEN, "")!!
    }

}