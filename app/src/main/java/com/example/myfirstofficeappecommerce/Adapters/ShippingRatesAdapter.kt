package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.WebViewFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import layout.CheckoutOverViewFragment
import java.util.concurrent.TimeUnit


class ShippingRatesAdapter(
    var context: Fragment,
    var checkoutId: String,
    var callback: (clickedPos: Int) -> Unit
) :
    ListAdapter<ModelClass, ShippingRatesAdapter.ShippingAddressChangeViewHolder>(ModelClass.diffUtil) {
    inner class ShippingAddressChangeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var shippingRatesNameTextView: TextView? =
            itemView.findViewById(R.id.shippingRatesHandleTextView)
        var radioButton: RadioButton? = itemView.findViewById(R.id.radioButton)
        var root: ConstraintLayout = itemView.findViewById(R.id.addressroot)
        var shippingPriceTextView: TextView? = itemView.findViewById(R.id.shippingPriceTextView)

        init {


            radioButton!!.setOnClickListener {
                var modelclass = currentList[absoluteAdapterPosition]
                var list = ArrayList(currentList)
                currentList.filter {
                    if (it.isSelectedAddress)
                        it.isSelectedAddress = false
                    return@filter true
                }

                modelclass.isSelectedAddress = true
                callback(absoluteAdapterPosition)
                notifyDataSetChanged()


                getNewWebAddressBasedOnShippingProvider(modelclass)


            }


        }

    }

     fun getNewWebAddressBasedOnShippingProvider(modelclass: ModelClass) {
        val query1 = mutation { m: MutationQuery ->
            m.checkoutShippingLineUpdate(
                ID(checkoutId), modelclass.subTitle,
                CheckoutShippingLineUpdatePayloadQueryDefinition { update: CheckoutShippingLineUpdatePayloadQuery ->

                    update.userErrors { errors: UserErrorQuery ->
                        errors.field().message()
                    }.checkout { _queryBuilder ->
                        _queryBuilder.webUrl()
                    }
                }
            )
        }

        var call1 =
            CategoriesDataProvider.graphh!!.mutateGraph(query1)
        call1.enqueue(object : GraphCall.Callback<Mutation> {


            override fun onResponse(response: GraphResponse<Mutation>) {

                ApplicationClass.weburl =
                    response.data()!!.checkoutShippingLineUpdate.checkout.webUrl


            }

            override fun onFailure(error: GraphError) {

            }

        })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingAddressChangeViewHolder {
        return ShippingAddressChangeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.change_shipping_rates_row_layout, parent, false)
        )

    }

    override fun submitList(list: MutableList<ModelClass>?) {
        super.submitList(list!!.toList())

    }

    override fun onBindViewHolder(holder: ShippingAddressChangeViewHolder, position: Int) {
        var modelClass: ModelClass = currentList[position]
        if(position==0) {
            modelClass.isSelectedAddress = true

        }

        holder.radioButton!!.isChecked = modelClass.isSelectedAddress
        holder.shippingPriceTextView!!.text = modelClass.shippingPrice
        holder.shippingRatesNameTextView!!.text = modelClass.title


    }
}