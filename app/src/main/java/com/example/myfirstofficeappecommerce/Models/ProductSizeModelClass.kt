package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class ProductSizeModelClass(var size:String?="",var variantId:String?="")
{
    companion object{
        var diffUtil:DiffUtil.ItemCallback<ProductSizeModelClass> = object : DiffUtil.ItemCallback<ProductSizeModelClass> (){
            override fun areItemsTheSame(
                oldItem: ProductSizeModelClass,
                newItem: ProductSizeModelClass
            ): Boolean {
                return oldItem.size==newItem.size
            }

            override fun areContentsTheSame(
                oldItem: ProductSizeModelClass,
                newItem: ProductSizeModelClass
            ): Boolean {
                return oldItem.size==newItem.size
            }
        }
    }
}