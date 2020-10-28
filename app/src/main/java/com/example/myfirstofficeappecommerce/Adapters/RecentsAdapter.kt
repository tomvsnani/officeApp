package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class RecentsAdapter :
    ListAdapter<VariantsModelClass, RecentsAdapter.RecentsViewHolder>(VariantsModelClass.diffUtil) {
    inner class RecentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recentItemNameTextView: TextView? = itemView.findViewById(R.id.recentitemNameTextView)
        var recentLocationNameTextView: TextView? =
            itemView.findViewById(R.id.recentlocationTextView)
        var recentItemsQuntityTextView: TextView? =
            itemView.findViewById(R.id.ordesrQunatityTextView)
        var recentPriceTextView: TextView? = itemView.findViewById(R.id.recentTotalAmountTextView)
        var recentDateTextView: TextView? = itemView.findViewById(R.id.recentDateTextView)
        var recentItemImageView: ImageView? = itemView.findViewById(R.id.recentItemImageView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentsViewHolder {
        return RecentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recents_row_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecentsViewHolder, position: Int) {
        var modelClass: VariantsModelClass = currentList[position]
        holder.recentDateTextView?.text = "Date Ordered: ${modelClass.dateOrdered}"
        holder.recentItemNameTextView?.text = modelClass.name
        holder.recentItemsQuntityTextView?.text =
            "Order Quantity : ${modelClass.quantityOfItem.toString()}"
        holder.recentLocationNameTextView?.text = modelClass.location
        holder.recentPriceTextView?.text =
            "Price : ${holder.recentPriceTextView!!.context.getString(R.string.Rs)} ${modelClass.price}"
        Glide.with(holder.recentItemImageView?.context!!).load(modelClass.imgSrc)
            .into(holder.recentItemImageView!!)
    }
}