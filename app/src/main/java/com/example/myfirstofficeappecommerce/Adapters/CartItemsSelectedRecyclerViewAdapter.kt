package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.CartFragment
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import kotlinx.android.synthetic.main.fragment_cart.view.*

class CartItemsSelectedRecyclerViewAdapter(var cartFragment: CartFragment) :
    androidx.recyclerview.widget.ListAdapter<CategoriesModelClass, CartItemsSelectedRecyclerViewAdapter.CardItemsSelectedViewHolder>(
        CategoriesModelClass.diffUtil
    ) {
    var totalSum:Int=cartFragment.count

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
            cartdeleteItemImageButton?.setOnClickListener {  var list: MutableList<CategoriesModelClass> = ArrayList(currentList)
                list.remove(currentList[adapterPosition])
                submitList(list)}

            cartItemremoveItemTextView?.setOnClickListener {
                var modelClass: CategoriesModelClass = currentList[adapterPosition]
                if (modelClass.quantityOfItem > 0) {

                    currentList[adapterPosition].quantityOfItem--
                    if (modelClass.quantityOfItem == 0) {
                        var list: MutableList<CategoriesModelClass> = ArrayList(currentList)
                        list.remove(modelClass)
                        submitList(list)
                    }
                    notifyItemChanged(adapterPosition)
                    totalSum -= modelClass.realTimeMrp.toInt()
                    cartFragment.totalAmountTextView?.text="Total : ${totalSum.toString()}"
                }


            }

            cartItemaddItemImagebutton?.setOnClickListener {
                currentList[adapterPosition].quantityOfItem++
                totalSum += (currentList[adapterPosition].realTimeMrp.toInt())
                notifyItemChanged(adapterPosition)
                cartFragment.totalAmountTextView?.text="Total : ${totalSum.toString()}"

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemsSelectedViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_selected_items_row_layout, parent, false)
        return CardItemsSelectedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardItemsSelectedViewHolder, position: Int) {
        var modelclass = currentList[position]
        holder.carItemQuantityTextView?.text = modelclass.quantityOfItem.toString()
        holder.cartItemPriceTextView?.text = modelclass.realTimeMrp
        holder.cartItemWeightTextView?.text = modelclass.itemNetWeight
        holder.cartItemnameTextView?.text = modelclass.itemName
        holder.cartQuequeNumberTextView?.text = modelclass.itemQueueNumber.toString()
    }
}