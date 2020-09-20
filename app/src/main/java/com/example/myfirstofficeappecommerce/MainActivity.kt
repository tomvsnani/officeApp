package com.example.myfirstofficeappecommerce

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myfirstofficeappecommerce.fragments.MainFragment
import com.example.myfirstofficeappecommerce.fragments.OrdersFragment
import com.example.myfirstofficeappecommerce.fragments.WishListFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null


    fun lockDrawer() {
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


    fun unlockDrawer() {
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

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

            if (menuItem.itemId == R.id.ordersmenu) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container,
                        OrdersFragment(ApplicationClass.selectedVariantList!!.filter {
                            it.isOrdered
                        })
                    ).addToBackStack(null)
                    .commit()
                return@setNavigationItemSelectedListener true
            }

            if (menuItem.itemId == R.id.wishlist) {


                            supportFragmentManager
                                .beginTransaction()
                                .replace(
                                    R.id.container,
                                   WishListFragment()
                                ).addToBackStack(null)
                                .commit()



                return@setNavigationItemSelectedListener true
            }
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}