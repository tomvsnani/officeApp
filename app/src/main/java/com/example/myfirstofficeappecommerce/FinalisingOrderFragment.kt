package com.example.myfirstofficeappecommerce

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.ChooseAddressRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.databinding.FragmentFinalisingOrderBinding
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.example.myfirstofficeappecommerce.fragments.NewAddressFragment
import com.example.myfirstofficeappecommerce.fragments.WebViewFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class FinalisingOrderFragment(var checkoutId: String) : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: ChooseAddressRecyclerAdapter? = null
    var binding: FragmentFinalisingOrderBinding? = null
    private var toolbar: Toolbar? = null
    var newAddressLayoutBinding: NewAddressLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.fragment_finalising_order, container, false)

        (activity as MainActivity).lockDrawer()

        toolbar = v?.findViewById(R.id.finalsingToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding = FragmentFinalisingOrderBinding.bind(v)
        adapter = ChooseAddressRecyclerAdapter(activity!!, checkoutId)
        recyclerView = v.findViewById(R.id.chooseaddressrecyclerview)
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = adapter

        retrieve_all_the_addresses()

        binding!!.addAddressFragment.setOnClickListener {
            binding!!.step1orderlinearlayout.visibility = View.GONE
            binding!!.newaddressinclude.root.visibility = View.VISIBLE

        }


        newAddressLayoutBinding = binding!!.newaddressinclude

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

                        binding!!.addressstepsinclude.checkoutview1.setBackgroundColor(
                            resources.getColor(
                                R.color.red
                            )
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
                                        .node as Payment).ready == false
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
            } else Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
        }




        binding!!.deliveroThisAddressButton.setOnClickListener {
            Toast.makeText(
                context,
                "Not yet implemented",
                Toast.LENGTH_SHORT
            ).show()
        }

        return v
    }

    private fun retrieve_all_the_addresses() {
        val query = query { rootQuery: QueryRootQuery ->
            rootQuery
                .customer(
                    (activity!!.getPreferences(Activity.MODE_PRIVATE).getString("token", ""))
                ) { _queryBuilder ->
                    _queryBuilder.addresses({ args: CustomerQuery.AddressesArguments? ->

                        args!!.first(10)
                    }, { _queryBuilder ->
                        _queryBuilder.edges { _queryBuilder ->
                            _queryBuilder.node { _queryBuilder ->
                                _queryBuilder.address1().city().province().zip().phone().firstName()
                                    .lastName().country()
                            }
                        }
                    })
                }
        }


        var call =
            CategoriesDataProvider.graphh!!.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<QueryRoot> {
            var addressList = ArrayList<ModelClass>()

            override fun onResponse(response: GraphResponse<QueryRoot>) {

                var address = response.data()!!.customer.addresses.edges

                for (i in address) {
                    addressList.add(
                        ModelClass(
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
                activity!!.runOnUiThread { adapter!!.submitList(addressList) }

            }

            override fun onFailure(error: GraphError) {

            }

        })
    }

}