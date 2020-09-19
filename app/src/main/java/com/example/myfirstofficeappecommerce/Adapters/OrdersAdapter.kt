package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class OrdersAdaptes :
    ListAdapter<VariantsModelClass, OrdersAdaptes.OrdersViewHolder>(VariantsModelClass.diffUtil) {
    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderItemNameTextView: TextView? = itemView.findViewById(R.id.itemNameTextView)
        var orderLocationNameTextView: TextView? = itemView.findViewById(R.id.locationTextView)
        var orderItemsQuntityTextView: TextView? =
            itemView.findViewById(R.id.ordesrQunatityTextView)
        var orderPriceTextView: TextView? = itemView.findViewById(R.id.ordersTotalAmountTextView)
        var orderDateTextView: TextView? = itemView.findViewById(R.id.ordersDateTextView)
        var orderItemImageView: ImageView? = itemView.findViewById(R.id.orderItemImageView)
        var orderRepeatTextView:TextView?=itemView.findViewById(R.id.ordersRepeatNowTextView)
        init {
            orderRepeatTextView?.setOnClickListener {  }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.orders_row_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        var modelClass: VariantsModelClass = currentList[position]
        holder.orderDateTextView?.text="Date Ordered: ${modelClass.dateOrdered}"
        holder.orderItemNameTextView?.text=modelClass.name
        holder.orderItemsQuntityTextView?.text="Order Quantity : ${modelClass.quantityOfItem.toString()}"
        holder.orderLocationNameTextView?.text=modelClass.location
        holder.orderPriceTextView?.text="Price : ${modelClass.price}"
    }
}