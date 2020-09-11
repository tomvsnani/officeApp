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
import com.example.myfirstofficeappecommerce.fragments.OrdersFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private var toolbar: androidx.appcompat.widget.Toolbar? = null

    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerlayout)
        navigationView = findViewById(R.id.navigationview)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            MainFragment()
        )
            .commit()

        navigationView?.setNavigationItemSelectedListener { menuItem ->
            if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout?.openDrawer(GravityCompat.START)
            }

            if (menuItem.itemId == R.id.ordersmenu)
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container,
                        OrdersFragment(ApplicationClass.selectedItemsList!!.filter {
                            it.isOrdered
                        })
                    ).addToBackStack(null)
                    .commit()
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}