package com.example.myfirstofficeappecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myfirstofficeappecommerce.fragments.MainFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var actionBarToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.maintoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById(R.id.drawerlayout)
        actionBarToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )
        (drawerLayout as DrawerLayout).addDrawerListener(actionBarToggle!!)
        (actionBarToggle as ActionBarDrawerToggle).syncState()

        supportFragmentManager.beginTransaction().replace(R.id.container,
            MainFragment()
        )
            .addToBackStack(null).commit()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}