package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class CategoriesModelClass(
    var itemCategory: String = "", var itemName: String = "", var itemDescriptionText: String? = "",
    var itemGrossWeight: String = "",
    var itemNetWeight: String = "",
    var offerMrp: String = "",
    var realTimeMrp: Int = 0,
    var offerPercentage: String = "",
    var timeToReceiveIt: String = "",
    var imageUrl: String = "ok",
    var quantityOfItem: Int = 0,
    var itemQueueNumber: Int = 0,
    var id: String = "",
    var groupId: String = "",
    var dateOrdered: String = "",
    var location: String = "",
    var isOrdered: Boolean = false,
    var imageSrc: List<ModelClass> = ArrayList(),
    var isFav: Boolean = false,
    var variantsList: MutableList<VariantsModelClass>? = ArrayList()
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
                return oldItem.itemName == newItem.itemName && oldItem.offerMrp == newItem.offerMrp
                        && oldItem.realTimeMrp == newItem.realTimeMrp && oldItem.timeToReceiveIt == newItem.timeToReceiveIt

            }
        }
    }
}