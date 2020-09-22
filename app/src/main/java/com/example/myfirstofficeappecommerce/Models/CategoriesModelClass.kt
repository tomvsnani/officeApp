package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class CategoriesModelClass(
    var itemCategory: String = "",
    var itemName: String = "",
    var itemDescriptionText: String? = "",
    var itemNetWeight: String = "",
    var realTimeMrp: Int = 0,
    var imageUrl: String = "ok",
    var quantityOfItem: Int = 0,
    var id: String = "",
    var groupId: String = "",
    var imageSrcOfVariants: List<ModelClass> = ArrayList(),
    var isFav: Boolean = false,
    var variantsList: MutableList<VariantsModelClass>? = ArrayList(),
    var cursor:String?="",
    var hasNextPage:Boolean=false,
    var categoryLink:String=""

    ) {
    companion object {
        val diffUtil: DiffUtil.ItemCallback<CategoriesModelClass> = object :
            DiffUtil.ItemCallback<CategoriesModelClass>() {
            override fun areItemsTheSame(
                oldItem: CategoriesModelClass,
                newItem: CategoriesModelClass
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.groupId == newItem.groupId && oldItem.quantityOfItem == newItem.quantityOfItem
            }

            override fun areContentsTheSame(
                oldItem: CategoriesModelClass,
                newItem: CategoriesModelClass
            ): Boolean {
                return oldItem.itemName == newItem.itemName
                        && oldItem.realTimeMrp == newItem.realTimeMrp

            }
        }
    }
}