package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R

class CategoriesEachRecyclerAdapter :ListAdapter<CategoriesModelClass,CategoriesEachRecyclerAdapter.CategoryViewHolder>(CategoriesModelClass.diffUtil){
    inner class CategoryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val itemImage:ImageView=itemView.findViewById(R.id.ItemImageView)
        val itemName:TextView=itemView.findViewById(R.id.ItemNameTextView)
        val itemDescription:TextView=itemView.findViewById(R.id.ItemDescriptionTextView)
        val itemGrossweight:TextView=itemView.findViewById(R.id.GrossWeightTextView)
        val itemNetWeight:TextView=itemView.findViewById(R.id.NetWeightTextView)
        val realmrp:TextView=itemView.findViewById(R.id.originalPriceTextView)
        val addToCart:Button=itemView.findViewById(R.id.addToCartButton)
        init {
            addToCart.setOnClickListener{
                Log.d("clicked","add to cart clicked")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       val view:View=LayoutInflater.from(parent.context)
           .inflate(R.layout.categories_each_viewpager_row_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
       var modelClass=currentList[position]
        holder.itemDescription.text=modelClass.itemDescriptionText
        holder.realmrp.text=modelClass.realTimeMrp
        holder.itemGrossweight.text=modelClass.itemGrossWeight
        holder.itemName.text=modelClass.itemName
        holder.itemNetWeight.text=modelClass.itemNetWeight
    }


}