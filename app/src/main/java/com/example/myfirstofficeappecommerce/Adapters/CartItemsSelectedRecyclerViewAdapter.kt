package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.fragments.CartFragment
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R

class CartItemsSelectedRecyclerViewAdapter(var cartFragment: CartFragment,var callback:(MutableList<CategoriesModelClass>)->Unit) :
    androidx.recyclerview.widget.ListAdapter<CategoriesModelClass, CartItemsSelectedRecyclerViewAdapter.CardItemsSelectedViewHolder>(
        CategoriesModelClass.diffUtil
    ) {
    var totalSum: Int = 0

    inner class CardItemsSelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartItemnameTextView: TextView? = itemView.findViewById(R.id.cartSelectedItemName)
        var cartQuequeNumberTextView: TextView? =
            itemView.findViewById(R.id.queuqnumberofselecteditemtextview)
        var cartItemWeightTextView: TextView? = itemView.findViewById(R.id.cartitemWeight)
        var cartItemPriceTextView: TextView? = itemView.findViewById(R.id.cartitemPrice)
        var carItemQuantityTextView: TextView? =
            itemView.findViewById(R.id.cartitemquantitiytextview)
        var cartdeleteItemImageButton: ImageButton? =
            itemView.findViewById(R.id.cartItemCloseImageButton)
        var cartItemremoveItemTextView: ImageButton? =
            itemView.findViewById(R.id.removeitemsImageButton)
        var cartItemaddItemImagebutton: ImageButton? =
            itemView.findViewById(R.id.additemsImageButton)


        init {

            cartdeleteItemImageButton?.setOnClickListener {
                var list: MutableList<CategoriesModelClass> = ArrayList(currentList)
                totalSum -=(currentList[adapterPosition].realTimeMrp.toInt())*currentList[adapterPosition].quantityOfItem
                currentList[adapterPosition].quantityOfItem=0
                 cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"
                list.remove(currentList[adapterPosition])
                if(adapterPosition>=0  && ApplicationClass.selectedItemsList!!.isNotEmpty())
                ( ApplicationClass.selectedItemsList as MutableList).removeAt(adapterPosition)
                submitList(list)
                callback(list.toMutableList())


            }

            cartItemremoveItemTextView?.setOnClickListener {
                var modelClass: CategoriesModelClass = currentList[adapterPosition]
                if (modelClass.quantityOfItem > 0) {

                    currentList[adapterPosition].quantityOfItem--
                    if (modelClass.quantityOfItem == 0) {
                        var list: MutableList<CategoriesModelClass> = currentList.toMutableList()
                        list.remove(modelClass)
                        if(adapterPosition>0&& ApplicationClass.selectedItemsList!!.isNotEmpty())
                        ( ApplicationClass.selectedItemsList as MutableList).removeAt(adapterPosition)
                        submitList(list)

                        callback(list.toMutableList())

                    }
                    notifyItemChanged(adapterPosition)
                    if(modelClass.quantityOfItem!=0)
                    totalSum -= modelClass.realTimeMrp.toInt()
                    cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"


                    Log.d(
                        "comparesize",
                        (ApplicationClass.selectedItemsList!!).toString()
                    )

                    Log.d(
                        "comparesizeokk",
                        (ApplicationClass.selectedItemsList!!.size).toString()
                    )

                }


            }

            cartItemaddItemImagebutton?.setOnClickListener {
                Log.d(
                    "comparesize",
                    (ApplicationClass.selectedItemsList!!).toString()
                )
                currentList[adapterPosition].quantityOfItem++

                Log.d(
                    "comparesizeokk",
                    (ApplicationClass.selectedItemsList!!).toString()
                )
                totalSum += (currentList[adapterPosition].realTimeMrp.toInt())
                notifyItemChanged(adapterPosition)
                cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"

                callback(currentList.toMutableList())

            }
        }


    }

    override fun submitList(list: MutableList<CategoriesModelClass>?) {

        totalSum = 0
        list?.filter {

            totalSum += (it.quantityOfItem * it.realTimeMrp.toInt())
            cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"

            return@filter true

        }

        super.submitList(list?.toList())

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemsSelectedViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_selected_items_row_layout, parent, false)
        return CardItemsSelectedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardItemsSelectedViewHolder, position: Int) {
        var modelclass = currentList[position]
        Log.d("sssss", modelclass.quantityOfItem.toString())
        holder.carItemQuantityTextView?.text = modelclass.quantityOfItem.toString()
        holder.cartItemPriceTextView?.text =   " MRP : ${cartFragment.getString(R.string.Rs)} ${modelclass.realTimeMrp}"
        holder.cartItemWeightTextView?.text = modelclass.itemNetWeight
        holder.cartItemnameTextView?.text = modelclass.itemName
        holder.cartQuequeNumberTextView?.text = modelclass.itemQueueNumber.toString()
    }
}