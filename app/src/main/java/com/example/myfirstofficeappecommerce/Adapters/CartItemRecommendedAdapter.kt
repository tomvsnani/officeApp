package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.fragments.CartFragment
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class CartItemRecommendedAdapter(var cartFragment: CartFragment, var callback:(VariantsModelClass)->Unit) :
    androidx.recyclerview.widget.ListAdapter<VariantsModelClass, CartItemRecommendedAdapter.CardItemsRecommendedViewHolder>(
       VariantsModelClass.diffUtil
    ) {


    override fun submitList(list: MutableList<VariantsModelClass>?) {
        super.submitList(list?.toList())
    }

    inner class CardItemsRecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartItemnameTextView: TextView? = itemView.findViewById(R.id.cartRecommendItemName)

        var cartItemWeightTextView: TextView? = itemView.findViewById(R.id.cartRecommendWeight)
        var cartItemRealPriceTextView: TextView? =
            itemView.findViewById(R.id.cartRecommendRealPriceMrp)
        var cartItemOfferPriceTextView: TextView? =
            itemView.findViewById(R.id.cartRecommendOfferMrp)
//        var cartItemOfferPercentPriceTextView: TextView? = itemView.findViewById(R.id.cartRecyclerviewRecommondedItems)


        var cartItemaddItemImagebutton: ImageButton? =
            itemView.findViewById(R.id.cartRecommendAddButton)


        init {


            cartItemaddItemImagebutton?.setOnClickListener {
              callback( currentList[adapterPosition])


            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardItemsRecommendedViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_recommendeditems_row_layout, parent, false)
        return CardItemsRecommendedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardItemsRecommendedViewHolder, position: Int) {
        var modelclass = currentList[position]
        //holder.cartItemOfferPriceTextView?.text = modelclass.pr.toString()
        holder.cartItemRealPriceTextView?.text = modelclass.price.toString()
//        holder.cartItemWeightTextView?.text = modelclass.itemNetWeight
        holder.cartItemnameTextView?.text = modelclass.name

    }
}