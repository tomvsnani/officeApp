package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.MainFragment
import com.example.myfirstofficeappecommerce.fragments.SearchFragment
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.LinkedHashMap

class searchfragmentRecyclerAdapter(
    var mainActivity: Fragment, var map: LinkedHashMap<String,
            List<CategoriesModelClass>>?, var viewType: String
) :
    ListAdapter<CategoriesModelClass, searchfragmentRecyclerAdapter.MainVieModel>(
        CategoriesModelClass.diffUtil
    ) {
    init {
        var list: LinkedList<CategoriesModelClass> = LinkedList()
        map!!.keys.toList().forEach { list.add(CategoriesModelClass(itemName = it)) }
        Log.d("remotead", "before")
        CategoriesDataProvider.mutableCollectionList.observeForever { submitList(it) }

    }

    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
        Log.d("remotead", list.toString())
        notifyDataSetChanged()
    }


    var list: List<CategoriesModelClass>? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {

        val int: Int =

            if (viewType == 0) R.layout.horizontalscrollitemnamerowlay else R.layout.search_fragment_row_layout

        val view: View = LayoutInflater.from(parent.context)

            .inflate(int, parent, false)

        return MainVieModel(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewType == Constants.SCROLL_TYPE)
            0
        else
            1
    }

    override fun onBindViewHolder(holder: MainVieModel, position: Int) {


        val model: CategoriesModelClass = currentList[position]
        Log.d("hello", model.itemName)

        if (viewType == Constants.SCROLL_TYPE)

            holder.textView?.text = model.itemName
        else {

            holder.searchFragmentitemNameTextView?.text = model.itemName

            holder.searchFragmentnumberOfPiecesTextView?.text =
                "pcs ${model.quantityOfItem.toString()}"

            holder.searchFragmentPriceTExtView?.text =
                "${mainActivity.getString(R.string.Rs)} ${model.realTimeMrp}"

            holder.searchQuantityTextView?.text = model.quantityOfItem.toString()

        }

    }

    inner class MainVieModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null

        var textView: TextView? = null

        var linearLayout: LinearLayout? = null

        var searchFragImageView: ImageView? = null

        var searchFragmentitemNameTextView: TextView? = null

        var searchFragmentnumberOfPiecesTextView: TextView? = null

        var searchFragmentPriceTExtView: TextView? = null

        var searchFragmentAddToCart: Button? = null

        var searchAddItemsQunatityImageView: ImageView? = null

        var searchRemoveItemsQuantityImageView: ImageView? = null

        var searchQuantityTextView: TextView? = null

        var addRemoveLinearLayout: LinearLayout? = null

        init {
            Log.d("clicked", itemViewType.toString())

            if (viewType == Constants.SCROLL_TYPE) {

                imageView = itemView.findViewById(R.id.HorizontalScrollItemNameImageView)

                textView = itemView.findViewById(R.id.HorizontalScrollItemNameTextView)

                linearLayout = itemView.findViewById(R.id.horizontalitemNameLinearLayout)

                linearLayout?.setOnClickListener {

                    Log.d("clicked", "yess")

                    mainActivity.activity!!.supportFragmentManager.beginTransaction()

                        .replace(R.id.container, CategoriesFragment(map, adapterPosition))
                        .addToBackStack(null)

                        .commit()
                }
            } else {

                searchFragImageView = itemView.findViewById(R.id.searchfragmentImageview)

                searchFragmentAddToCart = itemView.findViewById(R.id.searchfragmentaddtocart)

                searchFragmentPriceTExtView = itemView.findViewById(R.id.searchItemPriceTextview)

                searchFragmentitemNameTextView = itemView.findViewById(R.id.searchitemNametextView)

                addRemoveLinearLayout = itemView.findViewById(R.id.searchaddremovelinear)

                searchFragmentnumberOfPiecesTextView =

                    itemView.findViewById(R.id.searchItemPiecesTextView)

                searchAddItemsQunatityImageView =
                    itemView.findViewById(R.id.searchadditemsImageButton)

                searchRemoveItemsQuantityImageView =
                    itemView.findViewById(R.id.searchremoveitemsImageButton)

                searchQuantityTextView = itemView.findViewById(R.id.searchitemquantitiytextview)




                searchFragmentAddToCart!!.setOnClickListener {
                    currentList[adapterPosition].quantityOfItem++

                    searchFragmentAddToCart!!.visibility = View.GONE

                    addRemoveLinearLayout!!.visibility = View.VISIBLE

                    if (ApplicationClass.selectedItemsList?.contains(currentList[adapterPosition])!!)
                        ApplicationClass.selectedItemsList!!.find {
                            currentList[adapterPosition].groupId == it.groupId && currentList[adapterPosition].id == it.id
                        }!!.quantityOfItem = currentList[adapterPosition].quantityOfItem
                    else (ApplicationClass.selectedItemsList as MutableList).add(currentList[adapterPosition])
                    notifyItemChanged(adapterPosition)
                    (mainActivity as SearchFragment).showOrHideItemCountIndicator()
                }






                searchAddItemsQunatityImageView!!.setOnClickListener {
                    var modelClass = currentList[adapterPosition]
                    modelClass.quantityOfItem++
                    if (ApplicationClass.selectedItemsList?.contains(modelClass)!!)
                        ApplicationClass.selectedItemsList!!.find {
                            modelClass.groupId == it.groupId && modelClass.id == it.id
                        }!!.quantityOfItem = modelClass.quantityOfItem
                    else (ApplicationClass.selectedItemsList as MutableList).add(modelClass)
                    notifyItemChanged(adapterPosition)
                    (mainActivity as SearchFragment).showOrHideItemCountIndicator()
                }







                searchRemoveItemsQuantityImageView!!.setOnClickListener {
                    if (currentList[adapterPosition].quantityOfItem == 0) {
                        searchFragmentAddToCart!!.visibility = View.VISIBLE
                        addRemoveLinearLayout!!.visibility = View.GONE
                        if (ApplicationClass.selectedItemsList!!.contains(currentList[adapterPosition]))
                            (ApplicationClass.selectedItemsList as MutableList).remove(currentList[adapterPosition])
                    } else {
                        currentList[adapterPosition].quantityOfItem--
                        if (ApplicationClass.selectedItemsList!!.contains(currentList[adapterPosition]))
                            ApplicationClass.selectedItemsList?.find {
                                it.id == currentList[adapterPosition].id && it.groupId == currentList[adapterPosition].groupId
                            }!!.quantityOfItem =
                                currentList[adapterPosition].quantityOfItem
                    }
                    notifyItemChanged(adapterPosition)
                    (mainActivity as SearchFragment).showOrHideItemCountIndicator()

                }
            }


        }

    }


}





