package com.example.myfirstofficeappecommerce.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.ChatScreenFragment
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Models.ChatModel
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ChatRowLayoutBinding

class ChatAdapter(var fragment: ChatScreenFragment) :
    ListAdapter<ChatModel, ChatAdapter.ChatViewHolder>(ChatModel.diffutil) {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ChatRowLayoutBinding = ChatRowLayoutBinding.bind(itemView)


    }

    override fun submitList(list: MutableList<ChatModel>?) {
        super.submitList(list!!.toList())
        Thread.sleep(100)
        fragment.binding!!.chatrecyclerview.scrollToPosition(currentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_row_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var modelClass: ChatModel = currentList[position]
        holder.binding.chatmessagesentreceivedtimetextview.text = modelClass.message
        when (modelClass.messageStatus) {
            Constants.MESSAGE_STATUS_NOT_SENT -> Glide.with(fragment)
                .load(fragment.resources.getDrawable(R.drawable.ic_baseline_access_time_24))
                .into(holder.binding.messagestatusimagfeview)
            Constants.MESSAGE_STATUS_SENT -> Glide.with(fragment)
                .load(fragment.resources.getDrawable(R.drawable.ic_baseline_done_24))
                .into(holder.binding.messagestatusimagfeview)
            else ->
                Glide.with(fragment)
                    .load(fragment.resources.getDrawable(R.drawable.ic_baseline_done_all_24))
                    .into(holder.binding.messagestatusimagfeview)


        }
        holder.binding.chatmessagesentreceivedtimetextview.text = "8:30 AM"
        if (modelClass.from == "a") {
            holder.binding.chatconstraint.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                this.leftMargin = (fragment.resources.displayMetrics.widthPixels / 4)

                this.topMargin = dpFromPx(fragment.context!!, 4f)

            }


        } else {
            holder.binding.chatconstraint.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                this.rightMargin = (fragment.resources.displayMetrics.widthPixels / 4)

                this.topMargin = dpFromPx(fragment.context!!, 4f)
            }
        }
        holder.binding.messageTextView.text = modelClass.message
    }

    fun dpFromPx(context: Context, px: Float): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }
}