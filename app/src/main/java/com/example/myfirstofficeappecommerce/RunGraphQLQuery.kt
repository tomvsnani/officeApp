package com.example.myfirstofficeappecommerce

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.fragments.CartFragment
import com.example.myfirstofficeappecommerce.fragments.CheckOutMainWrapperFragment
import com.example.myfirstofficeappecommerce.fragments.loginFragment
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input
import java.util.concurrent.TimeUnit

class RunGraphQLQuery {
    companion object {
        fun getCheckoutData(mainActivity: MainActivity, signinType: String) {

            ApplicationClass.signInType = signinType

            val input = getCheckoutItemsFromSelectedItems()

            val query = checkoutCreateMutationQuery(input)

            CategoriesDataProvider.graphh!!.mutateGraph(query).enqueue(object :
                GraphCall.Callback<Storefront.Mutation> {

                override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                    if (response.data()!!.checkoutCreate.userErrors.isNotEmpty()) {

                        // handle user friendly errors
                    } else {

                        val checkoutId = response.data()!!.checkoutCreate.checkout.id.toString()

                        when {


                            signinType == Constants.NORMAL_SIGN_IN -> {
                                doSignedInTask(checkoutId, response)

                            }

                            else -> {
                                doNotSignedInTask(checkoutId, response)

                            }
                        }
                    }
                }

                private fun doNotSignedInTask(
                    checkoutId: String,
                    response: GraphResponse<Storefront.Mutation>
                ) {
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

                private fun doSignedInTask(
                    checkoutId: String,
                    response: GraphResponse<Storefront.Mutation>
                ) {
                    associateWithUserQuery(checkoutId, mainActivity)

                }


                override fun onFailure(error: GraphError) {

                }
            })

        }

        private fun checkoutCreateMutationQuery(input: Storefront.CheckoutCreateInput?): Storefront.MutationQuery? {
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
            return query
        }

        private fun getCheckoutItemsFromSelectedItems(): Storefront.CheckoutCreateInput? {
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
            return input
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
                                _queryBuilder.totalTax().webUrl()

                            }
                        }
                }

            CategoriesDataProvider.graphh!!.mutateGraph(associateCustomerQuery)
                .enqueue(object :
                    GraphCall.Callback<Storefront.Mutation> {
                    override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                        mainActivity.runOnUiThread {
                            mainActivity.binding!!.mainactivityprogressbar.visibility = View.GONE
                        }
                        val checkoutIdd =
                            response.data()!!.checkoutCustomerAssociate.checkout.id.toString()
                        val checkoutWebUrl =
                            response.data()!!.checkoutCustomerAssociate.checkout.webUrl

                        ApplicationClass.weburl = checkoutWebUrl

                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.container,
                                CheckOutMainWrapperFragment(
                                    checkoutIdd,
                                    response.data()!!.checkoutCustomerAssociate.checkout.totalTax.toFloat()

                                )
                            )
                            .addToBackStack(null).commit()

                    }

                    override fun onFailure(error: GraphError) {

                    }
                })
        }


        fun retrieve_all_the_addresses(activity: MainActivity): MutableLiveData<List<UserDetailsModelClass>> {

            val query = retrieveAllAddressesQuery(activity)
            var mutableAddressList = MutableLiveData<List<UserDetailsModelClass>>()

            var call =
                CategoriesDataProvider.graphh!!.queryGraph(query)
            call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {


                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {

                    var address = response.data()!!.customer.addresses.edges

                    for (i in address) {
                        ApplicationClass.addressList.add(
                            UserDetailsModelClass(
                                title = i.node.firstName,
                                subTitle = i.node.lastName,
                                hnum = i.node.address1,
                                city = i.node.city,
                                state = i.node.province,
                                pinCode = i.node.zip,
                                phoneNumber = if (i.node.phone == "" || i.node.phone.isNullOrBlank()) " no Phone number provided"
                                else i.node.phone, id = i.node.id.toString(),
                                country = i.node.country
                            )
                        )
                    }

                    ApplicationClass.defaultAdress = response.data()!!.customer.defaultAddress
                    mutableAddressList.postValue(ApplicationClass.addressList)
                }


                override fun onFailure(error: GraphError) {

                }

            },
                null,
                RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                    .whenResponse<Storefront.QueryRoot> { responsee: GraphResponse<Storefront.QueryRoot> ->
                        ((responsee as GraphResponse<Storefront.QueryRoot>).data()!!
                            .customer.addresses.edges.size == 0)
                    }
                    .maxCount(12)
                    .build())
            return mutableAddressList
        }

        private fun retrieveAllAddressesQuery(activity: MainActivity): Storefront.QueryRootQuery? {
            return Storefront.query { rootQuery: Storefront.QueryRootQuery ->
                rootQuery
                    .customer(
                        (activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", ""))
                    ) { _queryBuilder ->
                        _queryBuilder.addresses({ args: Storefront.CustomerQuery.AddressesArguments? ->

                            args!!.first(10)
                        }, { _queryBuilder ->

                            _queryBuilder.edges { _queryBuilder ->
                                _queryBuilder.node { _queryBuilder ->
                                    _queryBuilder.address1().city().province().zip().phone()
                                        .firstName()
                                        .lastName().country()
                                }
                            }
                        }).defaultAddress { _queryBuilder ->
                            _queryBuilder!!.address1().phone().name().province().country().zip()
                                .city().firstName()
                        }
                    }
            }
        }
    }


}
