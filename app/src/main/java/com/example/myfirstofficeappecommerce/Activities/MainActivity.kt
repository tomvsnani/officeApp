package com.example.myfirstofficeappecommerce.Activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.ExpandableMenuListViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.Item
import com.example.myfirstofficeappecommerce.Models.MenuJson
import com.example.myfirstofficeappecommerce.databinding.ActivityMainBinding
import com.example.myfirstofficeappecommerce.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity() : AppCompatActivity() {

    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null
    var menuList: List<CategoriesModelClass>? = ArrayList()
    var parentfragment: CheckOutMainWrapperFragment? = null
    var binding: ActivityMainBinding? = null
    var DISCOUNT = "FIRSTDISCOUNT"
    var navList: MutableList<Item> = ArrayList()
    var expandable_menu_adapter: ExpandableMenuListViewAdapter? = null
    var menudata: List<com.example.myfirstofficeappecommerce.Models.Menu> = ArrayList()


    companion object {
        var applyCoupon: Boolean = false
    }

    override fun onStart() {
        super.onStart()
        if ((application as ApplicationClass)!!.getCustomerToken(this).isNotEmpty())
            RunGraphQLQuery.retrieve_all_the_addresses(this)
    }

    override fun onBackPressed() {



        if (supportFragmentManager.backStackEntryCount == 0) {

            finish()

        } else {

            if (supportFragmentManager.findFragmentById(R.id.container) is MainFragment)

                finish()

            else

                super.onBackPressed()

        }
    }


    fun lockDrawer() {
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


    fun unlockDrawer() {
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initializeViews()

        getMenuData()


        binding!!.mainactivityprogressbar.visibility = View.GONE

        inflateMainFragment()

        setUpClickListeners()

    }

    private fun initializeViews() {
        menuList = ApplicationClass.menucategorylist
        drawerLayout = findViewById(R.id.drawerlayout)
        navigationView = findViewById(R.id.navigationview)
    }

    private fun getMenuData() {
        var retrofit = Retrofit.Builder().apply {
            //     baseUrl("https://my-json-server.typicode.com/tomvsnani/testfakerestapi/")
            baseUrl("http://127.0.0.1:8080/storage/downloads/")
            addConverterFactory(GsonConverterFactory.create())

        }.build()
        var call = retrofit.create(RetrofitInterface::class.java)

        var c = liveData<MenuJson>(Dispatchers.IO) {
            emit(getRetrofitData(call))
        }

        c.observe(this,
            Observer<MenuJson> { t ->
                Log.d("dataloaded", "ok");
                menudata = t!!.menu
                var hashMap: HashMap<com.example.myfirstofficeappecommerce.Models.Menu, List<Item>> =
                    HashMap()

                for (i in menudata.indices) {

                    hashMap[menudata[i]] = menudata[i].items

                    for (j in menudata[i].items.indices) {
                        menudata[i].items[j].parentIndexId = i
                        menudata[i].items[j].indexid = j

                        navList.add(menudata[i].items[j])
                    }
                }
                expandable_menu_adapter =
                    ExpandableMenuListViewAdapter(menudata, hashMap, this@MainActivity)
                binding!!.menuexpandablelist.setAdapter(expandable_menu_adapter)
                for (s in 0 until expandable_menu_adapter!!.groupCount) binding!!.menuexpandablelist.expandGroup(
                    s
                )
            })
    }


    private fun setUpClickListeners() {
        binding!!.menuexpandablelist.setOnGroupClickListener { parent, v, groupPosition, id ->
            return@setOnGroupClickListener true
        }


        binding!!.menuexpandablelist.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            opencloseDrawerLayout()

            if (menudata[groupPosition].items[childPosition].type == "general") {

                when (menudata[groupPosition].items[childPosition].name) {
                    "Chat" -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                ChatScreenFragment()
                            ).addToBackStack(null)
                            .commit()
                        return@setOnChildClickListener true
                    }

                    "Orders" -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            OrdersFragment(ApplicationClass.selectedVariantList!!)
                        ).addToBackStack(null)
                            .commit()
                        return@setOnChildClickListener true
                    }
                    "Recently viewed" -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                RecentsFragment(ApplicationClass.recentsList!!.filter {
                                    it.isRecent
                                })
                            ).addToBackStack(null)
                            .commit()
                        return@setOnChildClickListener true

                    }
                    "Wishlist" -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                WishListFragment()
                            ).addToBackStack(null)
                            .commit()

                        return@setOnChildClickListener true
                    }


                    "Home" -> {

                        opencloseDrawerLayout()
                        return@setOnChildClickListener true

                    }
                    "Myaccount" -> {
                        var fragment = MyAccountFragment()

                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            fragment
                        ).addToBackStack(null)
                            .commit()
                        return@setOnChildClickListener true

                    }
                    "Cart" -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            CartFragment(ApplicationClass.selectedVariantList)
                        ).addToBackStack(null)
                            .commit()

                        return@setOnChildClickListener true
                    }

                    "Webview" -> {

                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            WebViewFragment(
                                menudata[groupPosition].items[childPosition].url,
                                ""
                            )
                        ).addToBackStack(null)
                            .commit()
                        return@setOnChildClickListener true
                    }


                    else -> return@setOnChildClickListener true

                }


            } else if (menudata[groupPosition].items[childPosition].type == "collection") {

                Log.d(
                    "clicked",
                    " ok1"
                )

                if (menudata[groupPosition].items[childPosition].typeid.isNotEmpty()) {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            CategoryEachViewPagerFragment(
                                CategoriesModelClass(
                                    id = menudata[groupPosition].items[childPosition].typeid
                                )
                            ) {}
                        ).addToBackStack(null)
                        .commit()

                } else {


                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            CategoryEachViewPagerFragment(ApplicationClass.menucategorylist[0]) {}
                        ).addToBackStack(null)
                        .commit()


                }
            } else {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container, SubCategoryFragment(
                        menudata[groupPosition]
                            .items[childPosition].collection_items
                    )
                ).addToBackStack(null).commit()
            }
            return@setOnChildClickListener true
        }
    }

    private fun inflateMainFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            MainFragment()

        )

            .commit()
    }

    private suspend fun getRetrofitData(call: RetrofitInterface): MenuJson = call.getRestData()

    fun opencloseDrawerLayout() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("menucreatedact", "yes")
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return true
    }

    fun createCheckout(signinType: String) {
        RunGraphQLQuery.getCheckoutData(this, signinType)
    }
}