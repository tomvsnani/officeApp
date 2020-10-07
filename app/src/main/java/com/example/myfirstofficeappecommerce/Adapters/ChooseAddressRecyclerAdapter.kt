package com.example.myfirstofficeappecommerce.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.WebViewFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.Checkout
import com.shopify.buy3.Storefront.QueryRoot
import com.shopify.graphql.support.AbstractResponse
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class ChooseAddressRecyclerAdapter(var context: Context, var checkoutId: String) :
    ListAdapter<ModelClass, ChooseAddressRecyclerAdapter.ChooseAddressViewHolder>(ModelClass.diffUtil) {
    inner class ChooseAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ItemNameTextView: TextView? = itemView.findViewById(R.id.chooseAddressnameTextView)
        var LocationNameTextView: TextView? =
            itemView.findViewById(R.id.chooseAddressaddressTextView)

        var phnNumTextView: TextView? = itemView.findViewById(R.id.chooseAddressPhoneNumber)
        var editButton: Button? = itemView.findViewById(R.id.chooseAddressEditButton)
        var radioButton: RadioButton? = itemView.findViewById(R.id.radioButton)
        var root: ConstraintLayout = itemView.findViewById(R.id.addressroot)

        init {

            editButton?.setOnClickListener { }

            radioButton!!.setOnClickListener {
                var modelclass=currentList[absoluteAdapterPosition]
                var list = ArrayList(currentList)
                currentList.filter {
                    if (it.isSelectedAddress)
                        it.isSelectedAddress = false
                    return@filter true
                }
              modelclass.isSelectedAddress = true
                notifyDataSetChanged()

                val input = Storefront.MailingAddressInput()
                    .setAddress1(modelclass.hnum)
                    .setCity(modelclass.city)
                    .setFirstName(modelclass.title)
                    .setPhone(modelclass.phoneNumber)
                    .setProvince(modelclass.state)
                    .setZip(modelclass.pinCode)
                    .setLastName(modelclass.subTitle)
                    .setCountry(modelclass.country)


                val query1 = Storefront.mutation { mutationQuery: Storefront.MutationQuery ->
                    mutationQuery
                        .checkoutShippingAddressUpdate(
                            input, ID(checkoutId)
                        ) { shippingAddressUpdatePayloadQuery: Storefront.CheckoutShippingAddressUpdatePayloadQuery ->
                            shippingAddressUpdatePayloadQuery
                                .checkout { checkoutQuery: Storefront.CheckoutQuery ->
                                    checkoutQuery
                                        .webUrl()
                                }
                                .userErrors { userErrorQuery: Storefront.UserErrorQuery ->
                                    userErrorQuery
                                        .field()
                                        .message()
                                }
                        }

                }


                var call1 =
                    CategoriesDataProvider.graphh!!.mutateGraph(query1)
                call1.enqueue(object : GraphCall.Callback<Storefront.Mutation> {


                    override fun onResponse(response: GraphResponse<Storefront.Mutation>) {

                        val queryy = Storefront.query { rootQuery ->
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
                                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                                    val checkout = response.data()!!.node as Storefront.Checkout
                                    val shippingRates =
                                        checkout!!.availableShippingRates.shippingRates


                                }

                                override fun onFailure(error: GraphError) {
                                    Log.d("ratessf", error.message.toString())
                                }
                            },
                            null,
                            RetryHandler.exponentialBackoff(800, TimeUnit.MILLISECONDS, 1.2f)
                                .whenResponse<Storefront.QueryRoot> { response: GraphResponse<Storefront.QueryRoot> ->
                                    ((response as GraphResponse<QueryRoot>).data()!!
                                        .node as Checkout).availableShippingRates.ready == false
                                }
                                .maxCount(20)
                                .build()
                        )


                        (context as MainActivity).supportFragmentManager.beginTransaction()
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


            val query1 = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
                rootQuery
                    .customer(
                        ((context as Activity).getPreferences(Activity.MODE_PRIVATE)
                            .getString("token", ""))
                    ) { _queryBuilder ->
                        _queryBuilder.defaultAddress { _queryBuilder ->
                            _queryBuilder.address1().city().province().zip().phone().firstName().lastName().country()
                        }
                    }
            }


            var call1 =
                CategoriesDataProvider.graphh!!.queryGraph(query1)
            call1.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
                var addressList = ArrayList<ModelClass>()

                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {

                    var address = response.data()!!.customer.defaultAddress

                    var modelClass = ModelClass(
                        title = address.firstName,
                        subTitle = address.lastName,
                        hnum = address.address1,
                        city = address.city,
                        state = address.province,
                        pinCode = address.zip,
                        phoneNumber = if (address.phone == "" || address.phone.isNullOrBlank()) " no Phone number provided"
                        else address.phone, id = address.id.toString(),
                        country = address.country
                    )
                    currentList.find {
                        it.phoneNumber == modelClass.phoneNumber && it.subTitle == modelClass.subTitle && it.title == modelClass.title
                    }!!.isSelectedAddress = true
                    (context as Activity).runOnUiThread { notifyDataSetChanged() }

                }

                override fun onFailure(error: GraphError) {

                }

            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAddressViewHolder {
        return ChooseAddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.choose_shipping_address_row_layout, parent, false)
        )

    }

    override fun submitList(list: MutableList<ModelClass>?) {
        super.submitList(ArrayList(list))

    }

    override fun onBindViewHolder(holder: ChooseAddressViewHolder, position: Int) {
        var modelClass: ModelClass = currentList[position]

        holder.radioButton!!.isChecked = modelClass.isSelectedAddress
        holder.ItemNameTextView?.text = modelClass.title

        holder.LocationNameTextView?.text = modelClass.hnum+" "+modelClass.city+" \n"+modelClass.state+" "+modelClass.pinCode

        holder.phnNumTextView?.text = modelClass.phoneNumber


    }
}