package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil

data class UserDetailsModelClass(
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
    var shippingPrice:String="",
    var email:String=""

) {
    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<UserDetailsModelClass> =
            object : DiffUtil.ItemCallback<UserDetailsModelClass>() {
                override fun areItemsTheSame(oldItem: UserDetailsModelClass, newItem: UserDetailsModelClass): Boolean {
                    return oldItem.id == newItem.id && oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: UserDetailsModelClass, newItem: UserDetailsModelClass): Boolean {
                    return oldItem.subTitle == newItem.subTitle && oldItem.typeOfView == newItem.typeOfView
                            && oldItem.imageUrl == newItem.imageUrl
                }
            }
    }
}