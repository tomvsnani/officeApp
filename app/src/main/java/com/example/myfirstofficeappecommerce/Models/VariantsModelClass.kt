package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VariantsModelClass(
    @PrimaryKey
    var id: String = "",
    var parentProductId: String? = "",
    var color: String? = "",
    var size: String? = "",
    var price: Float? = -1f,
    var imgSrc: String? = "",
    var isSelected: Boolean = false,
    var isfav: Boolean = false,
    var quantityOfItem: Int = 0,
    var itemQueueNumber: Int = 0,
    var dateOrdered: String = "",
    var location: String = "",
    var isOrdered: Boolean = false,
    var name: String = "",
    var curserposition:String ="",
    var isRecent:Boolean=false,
    var orderId:String="",
    var isVariantAvailable:Boolean=true,
    var variantName:String=""



    ) {
    companion object {
        var diffUtil: DiffUtil.ItemCallback<VariantsModelClass> =
            object : DiffUtil.ItemCallback<VariantsModelClass>() {
                override fun areItemsTheSame(
                    oldItem: VariantsModelClass,
                    newItem: VariantsModelClass
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: VariantsModelClass,
                    newItem: VariantsModelClass
                ): Boolean {
                    return oldItem.color == newItem.color && oldItem.size == newItem.size && oldItem.imgSrc == newItem.imgSrc
                            && oldItem.price == newItem.price && oldItem.isSelected == newItem.isSelected && oldItem.name == newItem.name
                }

            }
    }
}