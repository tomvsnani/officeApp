package com.example.myfirstofficeappecommerce

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null
    var list: List<CategoriesModelClass>? = null
    var parentfragment:CheckOutActivity?=null


    override fun onBackPressed() {

        if(supportFragmentManager.backStackEntryCount==0)
            finish()
        else
            super.onBackPressed()
    }


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

        var a = navigationView!!.menu.addSubMenu(0, 1, 0, "Categories")
        CategoriesDataProvider.mutableCollectionList.observeForever { t ->
            list = t.toList()
            for (i in 0 until t.size) {
                a.add(1, i, 0, t[i].itemName)
            }
        }


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

            when {
                menuItem.itemId == R.id.ordersmenu -> {
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
                menuItem.itemId == R.id.recentmenu -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            RecentsFragment(ApplicationClass.recentsList!!.filter {
                                it.isRecent
                            })
                        ).addToBackStack(null)
                        .commit()
                    return@setNavigationItemSelectedListener true


                }
                menuItem.itemId == R.id.wishlistmenu -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            WishListFragment()
                        ).addToBackStack(null)
                        .commit()



                    return@setNavigationItemSelectedListener true
                }
                menuItem.groupId == 1 -> {
                    ApplicationClass.selectedTab = menuItem.itemId
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            CategoryEachViewPagerFragment(list?.get(menuItem.itemId), {})
                        )
                        .addToBackStack(null)

                        .commit()

                    return@setNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.homemenu -> {

                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        MainFragment()
                    )
                        .commit()
                    return@setNavigationItemSelectedListener true

                }
                menuItem.itemId == R.id.profilemenu -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container,
                        ProfileFragment(Constants.NORMAL_SIGN_IN)
                    ).addToBackStack(null)
                        .commit()

                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener true
            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}