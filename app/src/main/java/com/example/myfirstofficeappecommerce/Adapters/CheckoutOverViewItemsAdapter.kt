package com.example.myfirstofficeappecommerce.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.CheckoutOverviewRowLayoutBinding

class CheckoutOverViewItemsAdapter(var context: Context) :
    ListAdapter<VariantsModelClass, CheckoutOverViewItemsAdapter.CheckoutOverviewViewHolder>(VariantsModelClass.diffUtil) {
    inner class CheckoutOverviewViewHolder(var binding: CheckoutOverviewRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var ItemNameTextView: TextView? = binding.checkoutOverViewitemNameTextView
        var quantityTextView: TextView? = binding.checkoutOverViewQunatityTextView
        var priceTextView: TextView? =binding.checkoutoverviewpricetextview
        var itemImageView: ImageView? = binding.checkoutOverViewItemImageView



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutOverviewViewHolder {
        var binding=CheckoutOverviewRowLayoutBinding.bind(  LayoutInflater.from(parent.context).inflate(R.layout.checkout_overview_row_layout ,parent, false))
        return CheckoutOverviewViewHolder(
          binding
        )

    }

    override fun onBindViewHolder(holder: CheckoutOverviewViewHolder, position: Int) {
        var modelClass: VariantsModelClass = currentList[position]
        Log.d("modelclasss"," okkk")
        Log.d("modelclasss",modelClass.imgSrc.toString()+" okkk")

        holder.ItemNameTextView?.text=modelClass.name
        holder.quantityTextView?.text=modelClass.quantityOfItem.toString()
        holder.priceTextView?.text="Price : ${modelClass.price}"
        Glide.with(context).load(modelClass.imgSrc).into(holder.itemImageView!!)

    }
}