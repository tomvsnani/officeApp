package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class NewAddressFragment(var checkoutId: String, var webUrl: String, var totalTax: Float) :
    BottomSheetDialogFragment() {

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

        newAddressLayoutBinding!!.addAddressButton.setOnClickListener {

            if (checkIfAddressDetailsAreNotEmpty()) {

                getShippingRatesWrapper()

            } else

                Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
        }

        return v
    }

















    private fun checkIfAddressDetailsAreNotEmpty(): Boolean {

        return (newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank()
                &&
                newAddressLayoutBinding!!.emailEditText.text.toString().contains("@"))
    }

    private fun getShippingRatesWrapper() {
        getTheShippingRatesBasedOnSelectedAddress(
            newAddressLayoutBinding!!.cityEditText.text.toString(),
            newAddressLayoutBinding!!.cityEditText.text.toString(),
            newAddressLayoutBinding!!.nameEditText.text.toString(),
            newAddressLayoutBinding!!.lastnameEditText.text.toString(),
            newAddressLayoutBinding!!.PhonenumberEditText.text.toString(),
            newAddressLayoutBinding!!.provinceEditText.text.toString(),
            newAddressLayoutBinding!!.zipEditText.text.toString(),
            newAddressLayoutBinding!!.countryEditText.text.toString(),
            newAddressLayoutBinding!!.emailEditText.text.toString()
        )
    }


    private fun getTheShippingRatesBasedOnSelectedAddress(
        address1: String,
        city: String,
        fname: String,
        lname: String,
        phone: String,
        province: String,
        zip: String,
        country: String,
        email: String
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
            country = country,
            email = email
        )

        val shippingAddressUpdateQuery = getupdateShippingAddressQuery(input)

        var call =
            CategoriesDataProvider.graphh!!.mutateGraph(shippingAddressUpdateQuery)
        call.enqueue(object : GraphCall.Callback<Mutation> {


            override fun onResponse(response: GraphResponse<Mutation>) {

                associateEmailToShippingAddress(response, email, modelClass)


            }

            override fun onFailure(error: GraphError) {

            }

        })
    }




    private fun associateEmailToShippingAddress(
        response: GraphResponse<Mutation>,
        email: String,
        modelClass: UserDetailsModelClass
    ) {
        var associateEmailQuery = associateEmailQuery(response, email)
        var call1 =
            CategoriesDataProvider.graphh!!.mutateGraph(associateEmailQuery)
        call1.enqueue(object : GraphCall.Callback<Mutation> {


            override fun onResponse(response: GraphResponse<Mutation>) {
                var webUrll = response.data()!!.checkoutEmailUpdate.checkout.webUrl
                getShippingratesbasedOnAddress(
                    modelClass,
                    webUrll,
                    response.data()!!.checkoutEmailUpdate.checkout.id
                )
            }

            override fun onFailure(error: GraphError) {

            }
        })
    }

    private fun associateEmailQuery(
        response: GraphResponse<Mutation>,
        email: String
    ): MutationQuery? {
        return mutation { _queryBuilder ->
            _queryBuilder.checkoutEmailUpdate(
                response.data()!!.checkoutShippingAddressUpdate.checkout.id,
                email
            ) { _queryBuilder ->
                _queryBuilder.checkout { _queryBuilder ->
                    _queryBuilder.webUrl()
                }
            }
        }
    }

    private fun getupdateShippingAddressQuery(input: MailingAddressInput?): MutationQuery? {
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


    private fun getShippingratesbasedOnAddress(
        modelClass: UserDetailsModelClass,
        webUrl: String,
        id: ID
    ) {
        val queryy = query { rootQuery ->
            rootQuery
                .node(
                    id
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
                    if(!response.hasErrors() && response.data()!=null) {
                        val checkout = response.data()!!.node as Checkout
                        Log.d("ratessffl", response.errors().toString())
                        val shippingRates =
                            checkout.availableShippingRates.shippingRates
                        var c = 0f;
                        var userDetailsModelList: MutableList<UserDetailsModelClass> = ArrayList()
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
                                        this@NewAddressFragment.webUrl,
                                        userDetailsModelList,
                                        userDetailsModelList[0].shippingPrice.toFloat(),
                                        totalTax,
                                        modelClass,

                                        checkoutId
                                    )
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =

                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).skipCollapsed = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).isHideable = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).peekHeight = 0
        }
        return bottomSheetDialog
    }
}