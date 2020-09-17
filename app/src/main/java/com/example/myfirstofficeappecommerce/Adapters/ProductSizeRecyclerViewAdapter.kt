package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.ProductSizeModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class ProductSizeRecyclerViewAdapter(var callback:(size:String)->Unit):ListAdapter<VariantsModelClass,ProductSizeRecyclerViewAdapter.ProductSizeViewHolder>(VariantsModelClass.diffUtil){

    inner class ProductSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView:TextView=itemView.findViewById(R.id.productsizerowTextview)
        var cardView:CardView=itemView.findViewById(R.id.cardviewsize)
        init {
            textView.setOnClickListener {

                callback(currentList[adapterPosition].size!!)
             currentList.filter { it.isSelected=false
               return@filter true} as MutableList<VariantsModelClass>
                currentList[adapterPosition].isSelected=true
                notifyDataSetChanged()

        }
        }
    }

    override fun submitList(list: MutableList<VariantsModelClass>?) {
        super.submitList(list?.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSizeViewHolder {
      return  ProductSizeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_size_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ProductSizeViewHolder, position: Int) {
        if(currentList[position].isSelected)
            holder.cardView.cardElevation=20f
        else holder.cardView.cardElevation=0f
      holder.textView.text=currentList[position].size
    }
}