package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class NewAddressFragment(var checkoutId: String) : Fragment() {

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
                newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank()
            ) {

                val input = MailingAddressInput()
                    .setAddress1(newAddressLayoutBinding!!.cityEditText.text.toString())
                    .setCity(newAddressLayoutBinding!!.cityEditText.text.toString())
                    .setFirstName(newAddressLayoutBinding!!.nameEditText.text.toString())
                    .setPhone(newAddressLayoutBinding!!.PhonenumberEditText.text.toString())
                    .setProvince(newAddressLayoutBinding!!.provinceEditText.text.toString())
                    .setZip(newAddressLayoutBinding!!.zipEditText.text.toString())
                    .setLastName(newAddressLayoutBinding!!.lastnameEditText.text.toString())
                    .setCountry(newAddressLayoutBinding!!.countryEditText.text.toString())

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
                call.enqueue(object : GraphCall.Callback<Storefront.Mutation> {


                    override fun onResponse(response: GraphResponse<Mutation>) {

                        Log.d(
                            "webiddd",
                            response.data()!!.checkoutShippingAddressUpdate.checkout.webUrl
                        )


                        activity!!.runOnUiThread {
                            Toast.makeText(
                                context,
                                "Address updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


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
                                GraphCall.Callback<Storefront.QueryRoot> {
                                override fun onResponse(response: GraphResponse<QueryRoot>) {
                                    val checkout = response.data()!!.node as Checkout
                                    val shippingRates =
                                        checkout.availableShippingRates.shippingRates
                                    Log.d("ratess", shippingRates.toString())

                                }

                                override fun onFailure(error: GraphError) {
                                    Log.d("ratessf", error.message.toString())
                                }
                            },
                            null,
                            RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                                .whenResponse<Storefront.QueryRoot> { responsee: GraphResponse<Storefront.QueryRoot> ->
                                    ((responsee as GraphResponse<QueryRoot>).data()!!
                                        .node as Checkout).ready == false
                                }
                                .maxCount(12)
                                .build()
                        )


                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.container,
                                WebViewFragment(
                                    response.data()!!.checkoutShippingAddressUpdate.checkout.webUrl,
                                    "checkout"
                                )
                            )
                            .addToBackStack(null)
                            .commit()
                    }

                    override fun onFailure(error: GraphError) {

                    }

                })
            }
            else Toast.makeText(context,"Please enter all details",Toast.LENGTH_SHORT).show()
        }

        return v
    }
}