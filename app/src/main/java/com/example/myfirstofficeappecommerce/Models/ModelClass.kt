package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class ModelClass(
   val typeOfView:String,
    val title: String,
    val subTitle: String,
    val imageUrl: String?,
    val detailTest: String?

) {
    companion object {
        val diffUtil: DiffUtil.ItemCallback<ModelClass> =
            object : DiffUtil.ItemCallback<ModelClass>() {
                override fun areItemsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
                    return oldItem.subTitle == newItem.subTitle && oldItem.typeOfView == newItem.typeOfView
                            && oldItem.imageUrl == newItem.imageUrl
                }
            }
    }
}