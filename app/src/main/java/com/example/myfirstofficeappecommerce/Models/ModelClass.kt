package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class ModelClass(
    var typeOfView: String="",
    var title: String="",
    var subTitle: String="",
    var imageUrl: String?="",
    var detail: String?="",
    var id: String?="",
    var phoneNumber:String?="",
    var isSelectedAddress:Boolean=false,
    var pinCode:String="",
    var city:String="",
    var state:String="",
    var hnum:String="",
    var country:String="",
    var shippingPrice:String=""

) {
    companion object {
        val diffUtil: DiffUtil.ItemCallback<ModelClass> =
            object : DiffUtil.ItemCallback<ModelClass>() {
                override fun areItemsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
                    return oldItem.id == newItem.id && oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
                    return oldItem.subTitle == newItem.subTitle && oldItem.typeOfView == newItem.typeOfView
                            && oldItem.imageUrl == newItem.imageUrl
                }
            }
    }
}