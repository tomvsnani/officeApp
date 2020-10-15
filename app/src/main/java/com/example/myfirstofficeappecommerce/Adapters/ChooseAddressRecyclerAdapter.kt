package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.fragments.FinalisingOrderFragment
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.EditAddressFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.Checkout
import com.shopify.buy3.Storefront.QueryRoot
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class ChooseAddressRecyclerAdapter(var context: Fragment, var checkoutId: String) :
    ListAdapter<UserDetailsModelClass, ChooseAddressRecyclerAdapter.ChooseAddressViewHolder>(UserDetailsModelClass.DIFF_UTIL) {
    inner class ChooseAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ItemNameTextView: TextView? = itemView.findViewById(R.id.chooseAddressnameTextView)
        var LocationNameTextView: TextView? =
            itemView.findViewById(R.id.chooseAddressaddressTextView)

        var phnNumTextView: TextView? = itemView.findViewById(R.id.chooseAddressPhoneNumber)
        var editButton: Button? = itemView.findViewById(R.id.chooseAddressEditButton)
        var radioButton: RadioButton? = itemView.findViewById(R.id.radioButton)
        var root: ConstraintLayout = itemView.findViewById(R.id.addressroot)

        init {

            editButton?.setOnClickListener {

                (context as FinalisingOrderFragment).parentFragment!!.childFragmentManager.beginTransaction()
                    .replace(R.id.container1,EditAddressFragment(currentList[absoluteAdapterPosition]))
                    .addToBackStack(null).commit()

            }

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



                    }

                    override fun onFailure(error: GraphError) {

                    }

                })











            }


//            val query1 = Storefront.query { rootQuery: Storefront.QueryRootQuery ->
//                rootQuery
//                    .customer(
//                        ((context.activity!!).getPreferences(Activity.MODE_PRIVATE)
//                            .getString("token", ""))
//                    ) { _queryBuilder ->
//                        _queryBuilder.defaultAddress { _queryBuilder ->
//                            _queryBuilder.address1().city().province().zip().phone().firstName().lastName().country()
//                        }
//                    }
//            }
//
//
//            var call1 =
//                CategoriesDataProvider.graphh!!.queryGraph(query1)
//            call1.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
//                var addressList = ArrayList<ModelClass>()
//
//                override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
//
//                    var address = response.data()!!.customer.defaultAddress
//
//                    var modelClass = ModelClass(
//                        title = address.firstName,
//                        subTitle = address.lastName,
//                        hnum = address.address1,
//                        city = address.city,
//                        state = address.province,
//                        pinCode = address.zip,
//                        phoneNumber = if (address.phone == "" || address.phone.isNullOrBlank()) " no Phone number provided"
//                        else address.phone, id = address.id.toString(),
//                        country = address.country
//                    )
//                //    Log.d("isnull",(currentList.find {  it.id==address.id.toString()}==null).toString())
//                    currentList.find {
//                        it.phoneNumber == modelClass.phoneNumber && it.subTitle == modelClass.subTitle && it.title == modelClass.title
//                    }!!.isSelectedAddress = true
//                    (context.activity!!).runOnUiThread { notifyDataSetChanged() }
//
//                }
//
//                override fun onFailure(error: GraphError) {
//
//                }
//
//            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAddressViewHolder {
        return ChooseAddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.choose_shipping_address_row_layout, parent, false)
        )

    }

    override fun submitList(list: MutableList<UserDetailsModelClass>?) {
        super.submitList(list!!.toList())

    }

    override fun onBindViewHolder(holder: ChooseAddressViewHolder, position: Int) {
        var userDetailsModelClass: UserDetailsModelClass = currentList[position]

        holder.radioButton!!.isChecked = userDetailsModelClass.isSelectedAddress
        holder.ItemNameTextView?.text = userDetailsModelClass.title

        holder.LocationNameTextView?.text = userDetailsModelClass.hnum+" "+userDetailsModelClass.city+" \n"+userDetailsModelClass.state+" "+userDetailsModelClass.pinCode

        holder.phnNumTextView?.text = userDetailsModelClass.phoneNumber


    }
}