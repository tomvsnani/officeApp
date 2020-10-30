package com.example.myfirstofficeappecommerce.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.lifecycle.observe
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        CategoriesDataProvider.mutableCollectionList.observe(this) {
            ApplicationClass.menucategorylist = it

            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK) or (Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}