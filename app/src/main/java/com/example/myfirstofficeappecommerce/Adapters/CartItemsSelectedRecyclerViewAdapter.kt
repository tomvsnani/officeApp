package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.fragments.CartFragment
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.Utils
import kotlinx.android.synthetic.main.fragment_cart.*

class CartItemsSelectedRecyclerViewAdapter(
    private var cartFragment: CartFragment,
    var callback: (MutableList<VariantsModelClass>) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<VariantsModelClass, CartItemsSelectedRecyclerViewAdapter.CardItemsSelectedViewHolder>(
        VariantsModelClass.diffUtil
    ) {
    var totalSum: Float = 0f

    inner class CardItemsSelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartItemnameTextView: TextView? = itemView.findViewById(R.id.cartSelectedItemName)

        var cartItemPriceTextView: TextView? = itemView.findViewById(R.id.cartitemPrice)
        var carItemQuantityTextView: TextView? =
            itemView.findViewById(R.id.cartitemquantitiytextview)
        private var cartdeleteItemImageButton: ImageButton? =
            itemView.findViewById(R.id.cartItemCloseImageButton)
        private var cartItemremoveItemTextView: ImageButton? =
            itemView.findViewById(R.id.removeitemsImageButton)
        private var cartItemaddItemImagebutton: ImageButton? =
            itemView.findViewById(R.id.additemsImageButton)
        var variantType:TextView=itemView.findViewById(R.id.itemvarianttype)
        var itemImageView: ImageView = itemView.findViewById<ImageView>(R.id.cartImageView)


        init {

            cartdeleteItemImageButton?.setOnClickListener {
                var list: MutableList<VariantsModelClass> = ArrayList(currentList)
                totalSum -= (currentList[adapterPosition].price!!.toInt()) * currentList[adapterPosition].quantityOfItem
                currentList[adapterPosition].quantityOfItem = 0

                list.remove(currentList[adapterPosition])
                if (adapterPosition >= 0 && ApplicationClass.selectedVariantList!!.isNotEmpty()) {
                    (ApplicationClass.selectedVariantList as MutableList).removeAt(adapterPosition)

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
                        ApplicationClass.selectedVariantList?.remove(modelClass)
                        submitList(list)

                        callback(list.toMutableList())

                    }
                    notifyItemChanged(adapterPosition)
                    if (modelClass.quantityOfItem != 0)
                        totalSum -= modelClass.price!!.toInt()


                }
                assignTotalAmountToTextView()


            }

            cartItemaddItemImagebutton?.setOnClickListener {

                currentList[adapterPosition].quantityOfItem++


                totalSum += (currentList[adapterPosition].price!!.toInt())
                notifyItemChanged(adapterPosition)

                assignTotalAmountToTextView()

                callback(currentList.toMutableList())

            }
        }


    }

    override fun submitList(list: MutableList<VariantsModelClass>?) {

        totalSum = 0f
        list?.filter {

            totalSum += (it.quantityOfItem * it.price!!.toInt())


            return@filter true

        }
        if (list?.isEmpty()!!) {
            cartFragment.emptycartlayout!!.visibility = View.VISIBLE
            cartFragment.cartNestedScroll!!.visibility = View.GONE

        } else {
            cartFragment.emptycartlayout!!.visibility = View.GONE
            cartFragment.cartNestedScroll!!.visibility = View.VISIBLE
        }
        super.submitList(list?.toList())
        assignTotalAmountToTextView()

        notifyDataSetChanged()
    }

    private fun assignTotalAmountToTextView() {
        cartFragment.binding!!.ptotalAmountTextViewCart.text =
            "  ${cartFragment.activity!!.getString(R.string.Rs)} $totalSum"
        cartFragment.binding!!.cartOverViewTotalPriceTextView.text =
            "  ${cartFragment.activity!!.getString(R.string.Rs)} $totalSum"
        cartFragment.binding!!.totalitemstextview.text =
            "ITEMS ( ${Utils.getItemCount()} )"
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
        holder.cartItemPriceTextView?.text =
            " MRP : ${cartFragment.getString(R.string.Rs)} ${modelclass.price}"
        holder.cartItemnameTextView?.text = modelclass.name
        holder.variantType.text=modelclass.variantName
        Glide.with(cartFragment).load(modelclass.imgSrc).into(holder.itemImageView)

    }
}