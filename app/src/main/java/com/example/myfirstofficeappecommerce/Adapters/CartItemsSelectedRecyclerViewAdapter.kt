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
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R

class CartItemsSelectedRecyclerViewAdapter(var cartFragment: CartFragment,var callback:(MutableList<VariantsModelClass>)->Unit) :
    androidx.recyclerview.widget.ListAdapter<VariantsModelClass, CartItemsSelectedRecyclerViewAdapter.CardItemsSelectedViewHolder>(
        VariantsModelClass.diffUtil
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
                var list: MutableList<VariantsModelClass> = ArrayList(currentList)
                totalSum -=(currentList[adapterPosition].price!!.toInt())*currentList[adapterPosition].quantityOfItem
                currentList[adapterPosition].quantityOfItem=0
                 cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"
                list.remove(currentList[adapterPosition])
                if(adapterPosition>=0  && ApplicationClass.selectedVariantList!!.isNotEmpty()) {
                    (ApplicationClass.selectedVariantList as MutableList).removeAt(adapterPosition)
//                    (ApplicationClass.selectedItemsList as MutableList).remove( (ApplicationClass.selectedItemsList as MutableList)
//                        .find { it.id==currentList[adapterPosition].parentProductId })
                }
                submitList(list)
                callback(list.toMutableList())


            }

            cartItemremoveItemTextView?.setOnClickListener {
                var modelClass: VariantsModelClass = currentList[adapterPosition]
                if (modelClass.quantityOfItem > 0) {

                    currentList[adapterPosition].quantityOfItem--
                    if (modelClass.quantityOfItem == 0) {
                        var list: MutableList<VariantsModelClass> = currentList.toMutableList()
                        list.remove(modelClass)
                        if(adapterPosition>=0 && ApplicationClass.selectedVariantList!!.isNotEmpty()) {
                            Log.d("compareremoved",(ApplicationClass.selectedVariantList)!!.remove(currentList[adapterPosition]).toString())

//                            (ApplicationClass.selectedItemsList as MutableList).remove( (ApplicationClass.selectedItemsList as MutableList)
//                                .find { it.id==currentList[adapterPosition].parentProductId })
                        }
                        submitList(list)

                        callback(list.toMutableList())

                    }
                    notifyItemChanged(adapterPosition)
                    if(modelClass.quantityOfItem!=0)
                    totalSum -= modelClass.price!!.toInt()
                    cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"


                    Log.d(
                        "comparesizeremovee",
                        (ApplicationClass.selectedVariantList!!).toString()
                    )



                }


            }

            cartItemaddItemImagebutton?.setOnClickListener {
                Log.d(
                    "comparesize",
                    (ApplicationClass.selectedVariantList!!).toString()
                )
                currentList[adapterPosition].quantityOfItem++

                Log.d(
                    "comparesizeokk",
                    (ApplicationClass.selectedVariantList!!).toString()
                )
                totalSum += (currentList[adapterPosition].price!!.toInt())
                notifyItemChanged(adapterPosition)
                cartFragment.totalAmountTextView?.text = "Total : ${totalSum.toString()}"

                callback(currentList.toMutableList())

            }
        }


    }

    override fun submitList(list: MutableList<VariantsModelClass>?) {

        totalSum = 0
        list?.filter {

            totalSum += (it.quantityOfItem * it.price!!.toInt())
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
        holder.cartItemPriceTextView?.text =   " MRP : ${cartFragment.getString(R.string.Rs)} ${modelclass.price}"
        holder.cartItemnameTextView?.text = modelclass.name
        holder.cartQuequeNumberTextView?.text = modelclass.itemQueueNumber.toString()
    }
}