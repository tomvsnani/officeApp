package com.example.myfirstofficeappecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var progressbar: ProgressBar = findViewById(R.id.splashscreenprogressbar)
        CategoriesDataProvider.mutableCollectionList.observeForever { t ->
            //   progressbar.visibility = View.GONE
            ApplicationClass.selectedItemsList = t
            startActivity(Intent(this, MainActivity::class.java).apply {
                setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK) or (Intent.FLAG_ACTIVITY_NEW_TASK))
            })
        }
    }
}