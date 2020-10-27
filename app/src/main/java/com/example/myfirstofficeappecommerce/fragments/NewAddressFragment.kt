package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class NewAddressFragment(var checkoutId: String, var webUrl: String, var totalTax: Float) :
    Fragment() {

    var newAddressLayoutBinding: NewAddressLayoutBinding? = null
    private var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.new_address_layout, container, false);
        newAddressLayoutBinding = NewAddressLayoutBinding.bind(v)
        (activity as MainActivity).lockDrawer()

        toolbar = v?.findViewById(R.id.newaddressToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        newAddressLayoutBinding!!.addAddressButton.setOnClickListener {


            if (newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank()
            ) {
                getTheShippingRatesBasedOnSelectedAddress(
                    newAddressLayoutBinding!!.cityEditText.text.toString(),
                    newAddressLayoutBinding!!.cityEditText.text.toString(),
                    newAddressLayoutBinding!!.nameEditText.text.toString(),
                    newAddressLayoutBinding!!.lastnameEditText.text.toString(),
                    newAddressLayoutBinding!!.PhonenumberEditText.text.toString(),
                    newAddressLayoutBinding!!.provinceEditText.text.toString(),
                    newAddressLayoutBinding!!.zipEditText.text.toString(),
                    newAddressLayoutBinding!!.countryEditText.text.toString()
                )
            } else Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
        }



        return v
    }


    private fun getTheShippingRatesBasedOnSelectedAddress(
        address1: String,
        city: String,
        fname: String,
        lname: String,
        phone: String,
        province: String,
        zip: String,
        country: String
    ) {
        val input = MailingAddressInput()
            .setAddress1(address1)
            .setCity(city)
            .setFirstName(fname)
            .setPhone(phone)
            .setProvince(province)
            .setZip(zip)
            .setLastName(lname)
            .setCountry(country)


        val modelClass = UserDetailsModelClass(
            hnum = address1,
            city = city,
            title = fname,
            subTitle = lname,
            phoneNumber = phone,
            state = province,
            pinCode = zip,
            country = country
        )

        val query = mutation { mutationQuery: MutationQuery ->
            mutationQuery
                .checkoutShippingAddressUpdate(
                    input, ID(checkoutId)
                ) { shippingAddressUpdatePayloadQuery: CheckoutShippingAddressUpdatePayloadQuery ->
                    shippingAddressUpdatePayloadQuery
                        .checkout { checkoutQuery: CheckoutQuery ->
                            checkoutQuery
                                .webUrl()
                        }
                        .userErrors { userErrorQuery: UserErrorQuery ->
                            userErrorQuery
                                .field()
                                .message()
                        }
                }

        }


        var call =
            CategoriesDataProvider.graphh!!.mutateGraph(query)
        call.enqueue(object : GraphCall.Callback<Mutation> {


            override fun onResponse(response: GraphResponse<Mutation>) {
                for (i in
                response.data()!!.checkoutShippingAddressUpdate.userErrors
                )
                    Log.d(
                        "webiddd",
                        i.message
                    )
                Log.d("checkoutid", checkoutId)

                val queryy = query { rootQuery ->
                    rootQuery
                        .node(
                            ID(checkoutId)
                        ) { nodeQuery ->
                            nodeQuery
                                .onCheckout { checkoutQuery ->
                                    checkoutQuery
                                        .availableShippingRates { availableShippingRatesQuery ->
                                            availableShippingRatesQuery
                                                .ready()

                                                .shippingRates { shippingRateQuery ->
                                                    shippingRateQuery
                                                        .handle()
                                                        .price()
                                                        .title()

                                                }

                                        }
                                }

                        }
                }

                CategoriesDataProvider.graphh!!.queryGraph(queryy).enqueue(
                    object :
                        GraphCall.Callback<QueryRoot> {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onResponse(response: GraphResponse<QueryRoot>) {
                            val checkout = response.data()!!.node as Checkout
                            Log.d("ratessffl", response.errors().toString())
                            val shippingRates =
                                checkout.availableShippingRates.shippingRates
                            var c = 0f;
                            var userDetailsModelList:MutableList<UserDetailsModelClass> =ArrayList()
                            for (i in shippingRates) {
                                var model=UserDetailsModelClass(title = i.title,subTitle = i.handle,shippingPrice = i.price.precision().toString())
                                userDetailsModelList.add(model)
                                c += i.price.precision().toFloat()
                            }



                            activity!!.runOnUiThread {
                                parentFragment!!.childFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.container1,
                                        CheckoutOverViewFragment(webUrl, c, totalTax, modelClass,
                                            ApplicationClass.selectedVariantList!!,userDetailsModelList,checkoutId)
                                    )
                                    .addToBackStack(null)
                                    .commit()
                                Toast.makeText(
                                    context,
                                    "Address Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(error: GraphError) {
                            Log.d("ratessff", error.message.toString())
                        }
                    },
                    null,
                    RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                        .whenResponse<QueryRoot> { responsee: GraphResponse<QueryRoot> ->
                            ((responsee as GraphResponse<QueryRoot>).data()!!
                                .node as Checkout).ready == false
                        }
                        .maxCount(12)
                        .build()
                )

                webUrl = response.data()!!.checkoutShippingAddressUpdate.checkout.webUrl

            }

            override fun onFailure(error: GraphError) {

            }

        })
    }
}