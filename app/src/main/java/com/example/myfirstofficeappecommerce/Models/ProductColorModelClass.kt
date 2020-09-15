package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class ProductColorModelClass(var color:String?="",var variantId:String?="")
{
   companion object{
       var diffUtil: DiffUtil.ItemCallback<ProductColorModelClass> =object :
           DiffUtil.ItemCallback<ProductColorModelClass>(){
           override fun areItemsTheSame(
               oldItem: ProductColorModelClass,
               newItem: ProductColorModelClass
           ): Boolean {
               return  oldItem.color==newItem.color
           }

           override fun areContentsTheSame(
               oldItem: ProductColorModelClass,
               newItem: ProductColorModelClass
           ): Boolean {
               return  oldItem.color==newItem.color

           }
       }
   }
}