package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Adapters.ChooseAddressRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentFinalisingOrderBinding
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class FinalisingOrderFragment(var checkoutId: String, var totalTax: Float) : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: ChooseAddressRecyclerAdapter? = null
    var binding: FragmentFinalisingOrderBinding? = null
    private var toolbar: Toolbar? = null
    var newAddressLayoutBinding: NewAddressLayoutBinding? = null
    var webUrl: String = ""
    var addressList = ApplicationClass.addressList
    var bottomsheetFragment: BottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.fragment_finalising_order, container, false)

        setHasOptionsMenu(true)
        initialzeViews(v)

        if (ApplicationClass.signInType == Constants.NORMAL_SIGN_IN) {

            //retrieve_all_the_addresses()
            binding!!.addressconstraint.visibility = View.VISIBLE
            if(ApplicationClass.defaultAdress!=null)
            displaySelectedAddressDetails(ApplicationClass.defaultAdress!!)

            binding!!.addAddressButton.visibility = View.GONE
            binding!!.noaddresstextview.visibility = View.GONE

        } else {
            binding!!.addressconstraint.visibility = View.GONE
            binding!!.addAddressButton.visibility = View.VISIBLE
            binding!!.viewmoreaddressesbutton.visibility = View.GONE
            binding!!.noaddresstextview.visibility = View.VISIBLE
        }


        initializeClickListeners()

        return v
    }

    fun displaySelectedAddressDetails(defaultAdress: MailingAddress) {
        binding!!.chooseAddressnameTextView.text = defaultAdress?.name
        binding!!.chooseAddressPhoneNumber.text = defaultAdress?.phone
        binding!!.chooseAddressaddressTextView.text =
            defaultAdress?.city + " - " + defaultAdress?.zip + " \n \n" + defaultAdress?.province + " , " + defaultAdress?.country
    }

    private fun initializeClickListeners() {
        binding!!.viewmoreaddressesbutton.setOnClickListener {

            bottomsheetFragment = BottomSheetFragment(totalTax, checkoutId, webUrl, this)

            bottomsheetFragment!!.show(parentFragment!!.childFragmentManager, "")
        }

        binding!!.addAddressButton.setOnClickListener {
            bottomsheetFragment = BottomSheetFragment(totalTax, checkoutId, webUrl, this)

            bottomsheetFragment!!.show(parentFragment!!.childFragmentManager, "")
        }


        binding!!.deliveroThisAddressButton.setOnClickListener {


            var a =
                addressList.find { it.isSelectedAddress }

                    ?: addressList.find { it.id == ApplicationClass.defaultAdress?.id.toString() }
            if (a != null) {
                getTheShippingRatesBasedOnSelectedAddress(
                    a!!.hnum,
                    a!!.city,
                    a!!.title,
                    a.subTitle,
                    a.phoneNumber!!,
                    a.state,
                    a.pinCode,
                    a.country
                )
                binding!!.finalisingcheckoutfragmentprogressbar.visibility = View.VISIBLE
            } else
                Toast.makeText(
                    context,
                    "Please select an address or add an address",
                    Toast.LENGTH_SHORT
                ).show()
        }
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
                            val shippingRates =
                                checkout.availableShippingRates.shippingRates
                            var c = 0f;
                            var userDetailsModelList: MutableList<UserDetailsModelClass> =
                                ArrayList()
                            for (i in shippingRates) {
                                var model = UserDetailsModelClass(
                                    title = i.title,
                                    subTitle = i.handle,
                                    shippingPrice = i.price.precision().toString()
                                )
                                userDetailsModelList.add(model)
                                c += i.price.precision().toFloat()
                            }



                            activity!!.runOnUiThread {
                                parentFragment!!.childFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.container1,
                                        CheckoutOverViewFragment(
                                            webUrl,
                                            c,
                                            totalTax,
                                            modelClass,
                                            ApplicationClass.selectedVariantList!!,
                                            userDetailsModelList,
                                            checkoutId
                                        )
                                    )
                                    .addToBackStack(null)
                                    .commit()
                                Toast.makeText(
                                    context,
                                    "Address updated",
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

    private fun initialzeViews(v: View) {
        (activity as MainActivity).lockDrawer()

        toolbar = v?.findViewById(R.id.finalsingToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding = FragmentFinalisingOrderBinding.bind(v)
        adapter = ChooseAddressRecyclerAdapter(this, checkoutId)

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
                    }).defaultAddress { _queryBuilder ->
                        _queryBuilder!!.address1().phone()
                    }
                }
        }


        var call =
            CategoriesDataProvider.graphh!!.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<QueryRoot> {


            override fun onResponse(response: GraphResponse<QueryRoot>) {

                var address = response.data()!!.customer.addresses.edges

                for (i in address) {
                    addressList.add(
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



                activity!!.runOnUiThread {

                    var a =
                        addressList.find { it.id == response.data()!!.customer.defaultAddress.id.toString() }
                    if (a != null) {
                        a!!.isSelectedAddress = true

                        adapter!!.submitList(if (addressList.size > 0) mutableListOf(a) else null)

                        adapter!!.notifyDataSetChanged()
                    } else Toast.makeText(context!!, "Add an address", Toast.LENGTH_SHORT).show()


                }


            }

            override fun onFailure(error: GraphError) {

            }

        },
            null,
            RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                .whenResponse<QueryRoot> { responsee: GraphResponse<QueryRoot> ->
                    ((responsee as GraphResponse<QueryRoot>).data()!!
                        .customer.addresses.edges.size == 0)
                }
                .maxCount(12)
                .build())
    }

}