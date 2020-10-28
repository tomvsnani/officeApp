package com.example.myfirstofficeappecommerce

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.fragments.CartFragment
import com.example.myfirstofficeappecommerce.fragments.CheckOutMainWrapperFragment
import com.example.myfirstofficeappecommerce.fragments.ProfileFragment
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input

class RunGraphQLQuery {
    companion object {
        fun getCheckoutData(mainActivity: MainActivity, signinType: String) {
            ApplicationClass.signInType = signinType

            var checkoutLineItemInput: MutableList<Storefront.CheckoutLineItemInput>? = ArrayList()

            for (i in ApplicationClass.selectedVariantList!!) {
                checkoutLineItemInput?.add(
                    Storefront.CheckoutLineItemInput(
                        i.quantityOfItem,
                        ID(i.id)
                    )
                )
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
                            MainActivity.applyCoupon.toString()
                        )

                        if (MainActivity.applyCoupon)
                            applyDiscountQuery(checkoutId)
                        else
                            if (signinType == Constants.NORMAL_SIGN_IN) {
                                if (mainActivity.getPreferences(Activity.MODE_PRIVATE)
                                        ?.getString("token", "") != ""
                                )
                                    associateWithUserQuery(checkoutId, mainActivity)
                                else
                                    mainActivity.supportFragmentManager.beginTransaction()
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
                                mainActivity.supportFragmentManager.beginTransaction()
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
                            mainActivity.DISCOUNT,
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
                                    mainActivity.supportFragmentManager.findFragmentById(R.id.container)
                                if (f is CartFragment) {
                                    mainActivity.runOnUiThread {
                                        Toast.makeText(
                                            mainActivity.applicationContext,
                                            "Discount Applied",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        (f as CartFragment?)?.setTotalPriceAfterDiscount(
                                            response.data()
                                            !!.checkoutDiscountCodeApply.checkout.totalPrice.toString()
                                        )
                                        (f as CartFragment?)?.binding!!.applycoupontextview.setText(
                                            ""
                                        )
                                        (f as CartFragment?)?.binding!!.applycoupontextview.hint =
                                            "Discount Applied"
                                    }

                                }

                            } else {
                                if (response.data()!!.checkoutDiscountCodeApply.userErrors.isNotEmpty())
                                    for (i in response.data()!!.checkoutDiscountCodeApply.userErrors)
                                        mainActivity.runOnUiThread {
                                            Toast.makeText(
                                                mainActivity.applicationContext,
                                                i.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                            }
                        }

                        override fun onFailure(error: GraphError) {

                        }
                    })
                    MainActivity.applyCoupon = false
                }

                override fun onFailure(error: GraphError) {

                }
            })

        }


        private fun associateWithUserQuery(checkoutId: String, mainActivity: MainActivity) {
            var associateCustomerQuery =
                Storefront.mutation { mutationQuery: Storefront.MutationQuery ->
                    mutationQuery

                        .checkoutCustomerAssociate(
                            ID(checkoutId),
                            mainActivity.getPreferences(AppCompatActivity.MODE_PRIVATE)
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
                        mainActivity.supportFragmentManager.beginTransaction()
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
}
