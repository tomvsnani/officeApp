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

    var addressList = ApplicationClass.addressList
    var bottomsheetFragment: BottomSheetFragment? = null
    var userDetailsModelList: MutableList<UserDetailsModelClass> =
        ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAmination()
    }

    private fun setUpAmination() {
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

            performSignedInTasks()

        } else {

            performNotSignedInTask()
        }

        if (ApplicationClass.addressList.find { it.isSelectedAddress } != null) {
            toggleViewsOnSignIn()
            if (ApplicationClass.defaultAdress != null)
                displaySelectedAddressDetails(ApplicationClass.defaultAdress!!)
            getShippingRates()
        }

        initializeClickListeners()

        return v
    }


    private fun performNotSignedInTask() {
        binding!!.addressconstraint.visibility = View.GONE
        binding!!.addAddressButton.visibility = View.VISIBLE
        binding!!.viewmoreaddressesbutton.visibility = View.GONE
        binding!!.noaddresstextview.visibility = View.VISIBLE
        binding!!.shippingaddresslinear.visibility = View.GONE
    }

    fun performSignedInTasks() {
        retrieve_all_the_addresses()

        toggleViewsOnSignIn()
    }

    fun toggleViewsOnSignIn() {
        binding!!.addressconstraint.visibility = View.VISIBLE

        binding!!.addAddressButton.visibility = View.GONE
        binding!!.noaddresstextview.visibility = View.GONE
        binding!!.shippingaddresslinear.visibility = View.VISIBLE
        binding!!.viewmoreaddressesbutton.visibility = View.VISIBLE
    }

    fun displaySelectedAddressDetails(defaultAdress: MailingAddress) {
        binding!!.chooseAddressnameTextView.text = defaultAdress?.name
        binding!!.chooseAddressPhoneNumber.text = defaultAdress?.phone
        binding!!.chooseAddressaddressTextView.text =
            defaultAdress?.city + " - " + defaultAdress?.zip + " \n \n" + defaultAdress?.province + " , " + defaultAdress?.country
    }

    private fun initializeClickListeners() {
        binding!!.changeshippingratesimagebutton.setOnClickListener {
            selectShippingProviders()
        }

        binding!!.shippingratespricetextview.text

        binding!!.shippingratestitletextview.text


        binding!!.viewmoreaddressesbutton.setOnClickListener {

            bottomsheetFragment = BottomSheetFragment(totalTax, checkoutId, this)

            bottomsheetFragment!!.show(parentFragment!!.childFragmentManager, "")
        }

        binding!!.addAddressButton.setOnClickListener {
            bottomsheetFragment = BottomSheetFragment(totalTax, checkoutId, this)

            bottomsheetFragment!!.show(parentFragment!!.childFragmentManager, "")
        }


        binding!!.deliveroThisAddressButton.setOnClickListener {
            var a = addressList.find { it.isSelectedAddress }

                ?: addressList.find { it.id == ApplicationClass.defaultAdress?.id.toString() }
            if (a != null)
                try {
                    parentFragment!!.childFragmentManager.beginTransaction()
                        ?.replace(
                            R.id.container1,
                            CheckoutOverViewFragment(

                                userDetailsModelList,
                                binding!!.shippingratespricetextview.text.toString().toFloat(),
                                totalTax,
                                a!!,
                                checkoutId
                            )
                        )
                        .addToBackStack(null)
                        .commit()
                } catch (e: Exception) {
                    Log.d("error", e.printStackTrace().toString())
                }

        }
    }

    private fun getShippingRates() {
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

    private fun selectShippingProviders(

    ) {

        SelectShippingRatesBottomFragment(
            ApplicationClass.checkoutId,
            userDetailsModelList
        ) { i: Int ->
            var model = userDetailsModelList[0]
            displayShippingRates(model)


        }.show(activity!!.supportFragmentManager, "")


    }

    fun displayShippingRates(model: UserDetailsModelClass) {
        //  ApplicationClass.shippingratesAddressList=model
        binding!!.shippingratespricetextview.text = model.shippingPrice
        binding!!.shippingratestitletextview.text = model.title
        binding!!.finalisingcheckoutfragmentprogressbar.visibility = View.GONE
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

        val shippingAddressupdateQuery = shippingAddressUpdateQuery(input)


        var call =
            CategoriesDataProvider.graphh!!.mutateGraph(shippingAddressupdateQuery)
        call.enqueue(object : GraphCall.Callback<Mutation> {


            override fun onResponse(response: GraphResponse<Mutation>) {


                val queryy = shippingRatesQuery(response)

                CategoriesDataProvider.graphh!!.queryGraph(queryy).enqueue(
                    object :
                        GraphCall.Callback<QueryRoot> {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onResponse(response: GraphResponse<QueryRoot>) {
                            val checkout = response.data()!!.node as Checkout

                            //     ApplicationClass.checkoutId = checkout?.id?.toString()!!
//                            ApplicationClass.weburl = checkout.webUrl
                            val shippingRates =
                                checkout.availableShippingRates.shippingRates

                            userDetailsModelList.clear()
                            for (i in shippingRates) {
                                var model = UserDetailsModelClass(
                                    title = i.title,
                                    subTitle = i.handle,
                                    shippingPrice = i.price.toFloat().toString()
                                )
                                userDetailsModelList.add(model)

                            }
                            activity?.runOnUiThread { displayShippingRates(userDetailsModelList[0]) }

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


            }

            override fun onFailure(error: GraphError) {

            }

        })
    }

    private fun shippingRatesQuery(response: GraphResponse<Mutation>): QueryRootQuery? {
        return query { rootQuery ->
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

                                }.webUrl()
                        }

                }
        }
    }

    private fun shippingAddressUpdateQuery(input: MailingAddressInput?): MutationQuery? {
        return mutation { mutationQuery: MutationQuery ->
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
    }

    private fun initialzeViews(v: View) {
        (activity as MainActivity).lockDrawer()

        toolbar = v?.findViewById(R.id.finalsingToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding = FragmentFinalisingOrderBinding.bind(v)


    }

    fun retrieve_all_the_addresses() {

        val query = retrieveAddressesQuery()
        var call =
            CategoriesDataProvider.graphh!!.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<QueryRoot> {


            override fun onResponse(response: GraphResponse<QueryRoot>) {
                if (!response.hasErrors() && response.data() != null) {
                    var address = response.data()!!.customer.addresses.edges
                    addressList.clear()
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
                    ApplicationClass.defaultAdress = response.data()!!.customer.defaultAddress

                    doTasksBasedOnSelectedAddress(ApplicationClass.defaultAdress)

                }
            }

            override fun onFailure(error: GraphError) {
                Log.d("cameherefailed", error.message.toString())
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

    fun doTasksBasedOnSelectedAddress(defaultAdress: MailingAddress?) {
        activity?.runOnUiThread {
            if (defaultAdress != null) {
                displaySelectedAddressDetails(defaultAdress)
                getShippingRates()
            }

            //                        var a =
            //                            addressList.find { it.id == response.data()!!.customer.defaultAddress.id.toString() }
            //                        if (a != null) {
            //                            a!!.isSelectedAddress = true
            //
            //                            adapter!!.submitList(if (addressList.size > 0) mutableListOf(a) else null)
            //
            //                            adapter!!.notifyDataSetChanged()
            //                        } else Toast.makeText(context!!, "Add an address", Toast.LENGTH_SHORT)
            //                            .show()


        }
    }

    private fun retrieveAddressesQuery(): QueryRootQuery? {
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
                        _queryBuilder!!.address1().phone().name().province().country().zip()
                            .city().firstName()
                    }
                }
        }
        return query
    }

}