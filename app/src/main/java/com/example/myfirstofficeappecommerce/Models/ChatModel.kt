package com.example.myfirstofficeappecommerce.Models

import androidx.recyclerview.widget.DiffUtil
import com.example.myfirstofficeappecommerce.Constants

data class ChatModel(
    var from: String,
    var to: String,
    var time: String = "",
   var messageStatus:String=Constants.MESSAGE_STATUS_NOT_SENT,
    var id: Long,
    var message: String = ""
) {
    companion object {
        var diffutil = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem.messageStatus==newItem.messageStatus
            }
        }
    }
}