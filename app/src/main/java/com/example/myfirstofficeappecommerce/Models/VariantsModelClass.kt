package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class VariantsModelClass(
    var id: String? = "",
    var parentProductId: String? = "",
    var color: String? = "",
    var size: String? = "",
    var price: Float? = -1f,
    var imgSrc: String? = "",
var isSelected:Boolean=false
){
    companion object{
        var diffUtil:DiffUtil.ItemCallback<VariantsModelClass> = object :DiffUtil.ItemCallback<VariantsModelClass>(){
            override fun areItemsTheSame(
                oldItem: VariantsModelClass,
                newItem: VariantsModelClass
            ): Boolean {
               return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(
                oldItem: VariantsModelClass,
                newItem: VariantsModelClass
            ): Boolean {
                return oldItem.color==newItem.color && oldItem.size==newItem.size && oldItem.imgSrc==newItem.imgSrc
                        && oldItem.price==newItem.price && oldItem.isSelected==newItem.isSelected
            }

        }
    }
}