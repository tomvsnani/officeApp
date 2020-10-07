package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.CartFragment

class WishlistAdapter(var fragment:Fragment) :
    ListAdapter<VariantsModelClass, WishlistAdapter.WishListViewHolder>(VariantsModelClass.diffUtil) {
    inner class WishListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wishlistItemNameTextView: TextView? = itemView.findViewById(R.id.checkoutOverViewitemNameTextView)
        var wishlistLocationNameTextView: TextView? = itemView.findViewById(R.id.wishlistlocationTextView)
        var wishlistItemsQuntityTextView: TextView? =
            itemView.findViewById(R.id.wishlistQunatityTextView)
        var wishlistPriceTextView: TextView? = itemView.findViewById(R.id.wishlistTotalAmountTextView)
        var wishlistDateTextView: TextView? = itemView.findViewById(R.id.wishlistDateTextView)
        var wishlistItemImageView: ImageView? = itemView.findViewById(R.id.wishlistItemImageView)
        var buyNow:Button?=itemView.findViewById(R.id.wishlistbuybutton)

        init {

            buyNow!!.setOnClickListener {
                currentList[absoluteAdapterPosition].quantityOfItem++
                ApplicationClass.selectedVariantList!!.add(currentList[absoluteAdapterPosition])
                fragment.activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container,CartFragment(listOf(currentList[adapterPosition])))
                    .addToBackStack(null).commit()
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wishlist_wow_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        var modelClass: VariantsModelClass = currentList[position]
        Log.d("modelcq",modelClass.imgSrc!!)
        holder.wishlistDateTextView?.text="Date Ordered: ${modelClass.dateOrdered}"
        holder.wishlistItemNameTextView?.text=modelClass.name
        holder.wishlistItemsQuntityTextView?.text="Order Quantity : ${modelClass.quantityOfItem.toString()}"
        holder.wishlistLocationNameTextView?.text=modelClass.location
        holder.wishlistPriceTextView?.text="Price : ${modelClass.price}"
        Glide.with(fragment).load(modelClass.imgSrc).into(holder.wishlistItemImageView!!)
    }
}