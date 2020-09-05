package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class CategoriesModelClass(
    var itemCategory: String
    , var itemName: String
    , var itemDescriptionText: String,
    var itemGrossWeight: String,
    var itemNetWeight: String,
    var offerMrp: String,
    var realTimeMrp: String,
    var offerPercentage: String,
    var timeToReceiveIt: String
) {
    companion object {
        val diffUtil: DiffUtil.ItemCallback<CategoriesModelClass> = object :
            DiffUtil.ItemCallback<CategoriesModelClass>() {
            override fun areItemsTheSame(
                oldItem: CategoriesModelClass,
                newItem: CategoriesModelClass
            ): Boolean {
                return oldItem.itemCategory == newItem.itemCategory
            }

            override fun areContentsTheSame(
                oldItem: CategoriesModelClass,
                newItem: CategoriesModelClass
            ): Boolean {
                return oldItem.itemName == newItem.itemName && oldItem.offerMrp == newItem.offerMrp
                        && oldItem.realTimeMrp == newItem.realTimeMrp && oldItem.timeToReceiveIt == newItem.timeToReceiveIt
            }
        }
    }
}