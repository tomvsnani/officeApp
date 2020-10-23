package com.example.myfirstofficeappecommerce

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.Adapters.ExpandableMenuListViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.databinding.ActivityMainBinding
import com.example.myfirstofficeappecommerce.fragments.*
import com.google.android.material.navigation.NavigationView
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity() : AppCompatActivity() {

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null
    var list: List<CategoriesModelClass>? = ArrayList()
    var parentfragment: CheckOutMainWrapperFragment? = null
    var binding: ActivityMainBinding? = null
    var DISCOUNT = "FIRSTDISCOUNT"
    var navList: MutableList<Item> = ArrayList()
    var expandable_menu_adapter: ExpandableMenuListViewAdapter? = null
    var menudata: List<com.example.myfirstofficeappecommerce.Menu> = ArrayList()


    companion object {
        var applyCoupon: Boolean = false
    }


    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 0)
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        list = ApplicationClass.menucategorylist
        drawerLayout = findViewById(R.id.drawerlayout)
        navigationView = findViewById(R.id.navigationview)


//        val text = resources.openRawResource(R.raw.menu_new)
//            .bufferedReader().use { it.readText() }


        var retrofit = Retrofit.Builder().apply {
       //     baseUrl("https://my-json-server.typicode.com/tomvsnani/testfakerestapi/")
            baseUrl("http://127.0.0.1:8080/storage/downloads/")
            addConverterFactory(GsonConverterFactory.create())

        }.build()
        var call = retrofit.create(RetrofitInterface::class.java)
        call.getRestData().enqueue(object : Callback<MenuJson> {
            override fun onResponse(call: Call<MenuJson>, response: Response<MenuJson>) {
                var navmenu = navigationView!!.menu
                menudata = (response.body() as MenuJson).menu
                var hashMap: HashMap<com.example.myfirstofficeappecommerce.Menu, List<Item>> =
                    HashMap()

                for (i in menudata.indices) {
                    //     var menu = navmenu.addSubMenu(i, i, i, menudata[i].groupname)
                    hashMap[menudata[i]] = menudata[i].items



                    for (j in menudata[i].items.indices) {


                        menudata[i].items[j].parentIndexId = i
                        menudata[i].items[j].indexid = j

                        navList.add(menudata[i].items[j])
                        when (menudata[i].items[j].type) {
//                            "general", "collection" ->
//                            //menu.add(i, j, j, menudata[i].items[j].name)
////                                .apply {
////
////                                Log.d("rawjsonactivity", menudata[i].items[j].activity)
////
////                                if (menudata[i].items[j].icon.isNotEmpty())
////                                    Glide.with(applicationContext)
////                                        .asBitmap()
////                                        .load(menudata[i].items[j].icon)
////                                        .into(object : CustomTarget<Bitmap>() {
////                                            override fun onResourceReady(
////                                                resource: Bitmap,
////                                                transition: Transition<in Bitmap>?
////                                            ) {
////                                                icon = BitmapDrawable(resources, resource)
////                                            }
////
////                                            override fun onLoadCleared(placeholder: Drawable?) {
////
////                                            }
////                                        })
////
////                            }
//
//
////                            "nested" -> {
////                                //  var nestedmenu = menu.add(i, j, j, menudata[i].items[j].name)
////                                //           nestedmenu.actionView=layoutInflater.inflate(R.layout.menu_actionview_layout,null,false)
////                                //   menu.setHeaderIcon(resources.getDrawable(R.drawable.ic_baseline_add_24))
////                                //    menu.setIcon(resources.getDrawable(R.drawable.ic_baseline_add_24))
////                                //  for(k in menudata[i].items[j].nesteditems.indices)
//////                               nestedmenu.subMenu.add(0,1,2,"ok")
////                            }
                        }


                    }
                }
                expandable_menu_adapter =
                    ExpandableMenuListViewAdapter(menudata, hashMap, this@MainActivity)
                binding!!.menuexpandablelist.setAdapter(expandable_menu_adapter)
                for (s in 0 until expandable_menu_adapter!!.groupCount) binding!!.menuexpandablelist.expandGroup(
                    s
                )
            }

            override fun onFailure(call: Call<MenuJson>, t: Throwable) {
                Log.d("rawjson", t.message.toString())
                call.clone().enqueue(this)
            }
        })


//        for (i in list?.indices!!) {
//            a.add(1, i, 0, list!![i].itemName)
//        }

        binding!!.mainactivityprogressbar.visibility = View.GONE



        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            MainFragment()
        )
            .commit()

        binding!!.menuexpandablelist.setOnGroupClickListener { parent, v, groupPosition, id ->
            return@setOnGroupClickListener true
        }


        binding!!.menuexpandablelist.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Log.d(
                "clicked",
                menudata[groupPosition].items[childPosition].type
            )

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
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.container,
                                OrdersFragment(ApplicationClass.selectedVariantList!!.filter {
                                    it.isOrdered
                                })
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

                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            MainFragment()
                        )
                            .commit()
                        return@setOnChildClickListener true

                    }
                    "Myaccount" -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.container,
                            ProfileFragment(Constants.NORMAL_SIGN_IN)
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
            }
            else{
                supportFragmentManager.beginTransaction().replace(R.id.container,SubCategoryFragment(menudata[groupPosition]
                    .items[childPosition].collection_items)).commit()
            }
            return@setOnChildClickListener true
        }

    }

    fun opencloseDrawerLayout() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun createCheckout(signinType: String) {
        ApplicationClass.signInType = signinType

        var checkoutLineItemInput: MutableList<Storefront.CheckoutLineItemInput>? = ArrayList()

        for (i in ApplicationClass.selectedVariantList!!) {
            checkoutLineItemInput?.add(Storefront.CheckoutLineItemInput(i.quantityOfItem, ID(i.id)))
            i.isOrdered = true
        }

        val input = Storefront.CheckoutCreateInput()
            .setLineItemsInput(
                Input.value(
                    checkoutLineItemInput
                )
            )

        val query = Storefront.mutation { mutationQuery: Storefront.MutationQuery ->
            mutationQuery

                .checkoutCreate(
                    input
                ) { createPayloadQuery: Storefront.CheckoutCreatePayloadQuery ->
                    createPayloadQuery
                        .checkout { checkoutQuery: Storefront.CheckoutQuery ->
                            checkoutQuery
                                .webUrl()
                                .totalTax()
                                .totalPrice()
                                .subtotalPrice()

                        }
                        .userErrors { userErrorQuery: Storefront.UserErrorQuery ->
                            userErrorQuery
                                .field()
                                .message()
                        }
                }
        }

        CategoriesDataProvider.graphh!!.mutateGraph(query).enqueue(object :
            GraphCall.Callback<Storefront.Mutation> {

            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                if (response.data()!!.checkoutCreate.userErrors.isNotEmpty()) {

                    // handle user friendly errors
                } else {

                    val checkoutId = response.data()!!.checkoutCreate.checkout.id.toString()

                    Log.d(
                        "discount",
                        applyCoupon.toString()
                    )

                    if (applyCoupon)
                        applyDiscountQuery(checkoutId)
                    else
                        if (signinType == Constants.NORMAL_SIGN_IN) {
                            if (getPreferences(Activity.MODE_PRIVATE)
                                    ?.getString("token", "") != ""
                            )
                                associateWithUserQuery(checkoutId)
                            else
                                supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.container,

                                        ProfileFragment(
                                            Constants.NORMAL_SIGN_IN,
                                            fragment = CheckOutMainWrapperFragment(
                                                checkoutId, response.data()!!.checkoutCreate
                                                    .checkout.totalTax.toFloat()
                                            )
                                        )
                                    ).addToBackStack(null).commit()
                        } else {
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.container,
                                    CheckOutMainWrapperFragment(
                                        checkoutId,
                                        response.data()!!.checkoutCreate.checkout.totalTax.precision()
                                            .toFloat()
                                    )
                                ).addToBackStack(null).commit()

                        }
                }
            }

            private fun applyDiscountQuery(checkoutId: String) {
                var queryy = Storefront.mutation { _queryBuilder ->
                    _queryBuilder.checkoutDiscountCodeApply(
                        DISCOUNT,
                        ID(checkoutId)
                    ) { _queryBuilder ->
                        _queryBuilder.checkout { _queryBuilder ->
                            _queryBuilder.totalPrice()
                        }.userErrors { _queryBuilder ->
                            _queryBuilder.field().message()
                        }
                    }
                }

                CategoriesDataProvider.graphh!!.mutateGraph(queryy).enqueue(object :
                    GraphCall.Callback<Storefront.Mutation> {

                    override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                        if (!response.hasErrors() && response.data()!!.checkoutDiscountCodeApply.userErrors.isEmpty()) {

                            val f: Fragment? =
                                supportFragmentManager.findFragmentById(R.id.container)
                            if (f is CartFragment) {
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Discount Applied",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    (f as CartFragment?)?.setTotalPriceAfterDiscount(
                                        response.data()
                                        !!.checkoutDiscountCodeApply.checkout.totalPrice.toString()
                                    )
                                    (f as CartFragment?)?.binding!!.applycoupontextview.setText("")
                                    (f as CartFragment?)?.binding!!.applycoupontextview.hint =
                                        "Discount Applied"
                                }

                            }

                        } else {
                            if (response.data()!!.checkoutDiscountCodeApply.userErrors.isNotEmpty())
                                for (i in response.data()!!.checkoutDiscountCodeApply.userErrors)
                                    runOnUiThread {
                                        Toast.makeText(
                                            applicationContext,
                                            i.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                        }
                    }

                    override fun onFailure(error: GraphError) {

                    }
                })
                applyCoupon = false
            }

            override fun onFailure(error: GraphError) {

            }
        })

    }


    private fun associateWithUserQuery(checkoutId: String) {
        var associateCustomerQuery =
            Storefront.mutation { mutationQuery: Storefront.MutationQuery ->
                mutationQuery

                    .checkoutCustomerAssociate(
                        ID(checkoutId),
                        getPreferences(MODE_PRIVATE)
                            .getString("token", "")
                    ) {

                            _queryBuilder ->
                        _queryBuilder.checkout { _queryBuilder ->
                            _queryBuilder.totalTax()

                        }
                    }
            }

        CategoriesDataProvider.graphh!!.mutateGraph(associateCustomerQuery)
            .enqueue(object :
                GraphCall.Callback<Storefront.Mutation> {
                override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                    val checkoutIdd =
                        response.data()!!.checkoutCustomerAssociate.checkout.id.toString()
                    val checkoutWebUrl =
                        response.data()!!.checkoutCustomerAssociate.checkout.webUrl
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            CheckOutMainWrapperFragment(
                                checkoutIdd,
                                response.data()!!.checkoutCustomerAssociate.checkout.totalTax.precision()
                                    .toFloat()
                            )
                        )
                        .addToBackStack(null).commit()

                }

                override fun onFailure(error: GraphError) {

                }
            })
    }
}