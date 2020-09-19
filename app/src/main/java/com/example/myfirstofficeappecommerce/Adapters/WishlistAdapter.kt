package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class WishlistAdapter :
    ListAdapter<VariantsModelClass, WishlistAdapter.WishListViewHolder>(VariantsModelClass.diffUtil) {
    inner class WishListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wishlistItemNameTextView: TextView? = itemView.findViewById(R.id.wishlistitemNameTextView)
        var wishlistLocationNameTextView: TextView? = itemView.findViewById(R.id.wishlistlocationTextView)
        var wishlistItemsQuntityTextView: TextView? =
            itemView.findViewById(R.id.wishlistQunatityTextView)
        var wishlistPriceTextView: TextView? = itemView.findViewById(R.id.wishlistTotalAmountTextView)
        var wishlistDateTextView: TextView? = itemView.findViewById(R.id.wishlistDateTextView)
        var wishlistItemImageView: ImageView? = itemView.findViewById(R.id.wishlistItemImageView)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wishlist_wow_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        var modelClass: VariantsModelClass = currentList[position]
        holder.wishlistDateTextView?.text="Date Ordered: ${modelClass.dateOrdered}"
        holder.wishlistItemNameTextView?.text=modelClass.name
        holder.wishlistItemsQuntityTextView?.text="Order Quantity : ${modelClass.quantityOfItem.toString()}"
        holder.wishlistLocationNameTextView?.text=modelClass.location
        holder.wishlistPriceTextView?.text="Price : ${modelClass.price}"
    }
}