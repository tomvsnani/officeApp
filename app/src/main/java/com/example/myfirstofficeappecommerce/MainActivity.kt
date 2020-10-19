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


class MainActivity() : AppCompatActivity() {

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarToggle: ActionBarDrawerToggle? = null
    var list: List<CategoriesModelClass>? = ArrayList()
    var parentfragment: CheckOutMainWrapperFragment? = null
    var binding: ActivityMainBinding? = null
    var DISCOUNT = "FIRSTDISCOUNT"

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

        var a = navigationView!!.menu.addSubMenu(0, 1, 1, "Categories")

        for (i in list?.indices!!) {
            a.add(1, i, 0, list!![i].itemName)
        }

        binding!!.mainactivityprogressbar.visibility = View.GONE



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
                menuItem.itemId == R.id.chatscreen -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.container,
                            ChatScreenFragment()
                        ).addToBackStack(null)
                        .commit()
                    return@setNavigationItemSelectedListener true
                }

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