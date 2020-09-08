package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class MainRecyclerAdapter(
    var mainActivity: MainActivity, var map: LinkedHashMap<String,
            List<CategoriesModelClass>>?, var viewType: String
) :
    ListAdapter<CategoriesModelClass, MainRecyclerAdapter.MainVieModel>(
        CategoriesModelClass.diffUtil
    )

{
    init {
        var list:LinkedList<CategoriesModelClass> = LinkedList()
       map!!.keys.toList().forEach { list.add(CategoriesModelClass(itemName = it) )}
        submitList(list)
    }

    var list: List<CategoriesModelClass>? = null;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {
        val int: Int =
            if (viewType == 0) R.layout.horizontalscrollitemnamerowlay else R.layout.mainrecyclerviewrowlayout2
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
        if (viewType == Constants.SCROLL_TYPE)
            holder.textView?.text = model.itemName
        else {
            holder.searchFragmentitemNameTextView?.text = model.itemName
            holder.searchFragmentnumberOfPiecesTextView?.text= model.quantityOfItem.toString()
            holder.searchFragmentPriceTExtView?.text=model.realTimeMrp

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

        init {
            Log.d("clicked", itemViewType.toString())
            if (viewType == Constants.SCROLL_TYPE) {
                imageView = itemView.findViewById(R.id.HorizontalScrollItemNameImageView)
                textView = itemView.findViewById(R.id.HorizontalScrollItemNameTextView)
                linearLayout = itemView.findViewById(R.id.horizontalitemNameLinearLayout)

                linearLayout?.setOnClickListener {
                    Log.d("clicked", "yess")

                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CategoriesFragment(map)).addToBackStack(null)
                        .commit()
                }
            } else {
                searchFragImageView = itemView.findViewById(R.id.searchfragmentImageview)
                searchFragmentAddToCart = itemView.findViewById(R.id.searchfragmentaddtocart)
                searchFragmentPriceTExtView = itemView.findViewById(R.id.searchItemPriceTextview)
                searchFragmentitemNameTextView = itemView.findViewById(R.id.searchitemNametextView)
                searchFragmentnumberOfPiecesTextView =
                    itemView.findViewById(R.id.searchItemPiecesTextView)
            }

        }


    }


}



