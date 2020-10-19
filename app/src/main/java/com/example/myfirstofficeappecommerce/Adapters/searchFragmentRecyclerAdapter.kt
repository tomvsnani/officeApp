package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment
import com.example.myfirstofficeappecommerce.fragments.ProductFragment
import com.example.myfirstofficeappecommerce.fragments.SearchFragment

class searchfragmentRecyclerAdapter(
    var mainActivity: SearchFragment, var viewType: String
) :
    ListAdapter<CategoriesModelClass, searchfragmentRecyclerAdapter.MainVieModel>(
        CategoriesModelClass.diffUtil
    ) {


    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
        Log.d("remotead", list.toString())
        notifyDataSetChanged()
    }


    var list: List<CategoriesModelClass>? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {

        val int: Int =

            if (viewType == 0) R.layout.collections_row_layout else R.layout.search_fragment_row_layout

        val view: View = LayoutInflater.from(parent.context)

            .inflate(int, parent, false)

        return MainVieModel(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewType == Constants.SEARCH_FRAG_SCROLL_TYPE)
            0
        else
            1
    }

    override fun onBindViewHolder(holder: MainVieModel, position: Int) {


        val model: CategoriesModelClass = currentList[position]
        Log.d("hello", model.itemName)

        if (viewType == Constants.SEARCH_FRAG_SCROLL_TYPE)
        {
            holder.textView?.text = model.itemName
            Glide.with(mainActivity).load(model.imageUrl).circleCrop().into(holder.imageView!!)
    }
        else {

            holder.searchFragmentitemNameTextView?.text = model.itemName

            holder.searchFragmentnumberOfPiecesTextView?.text =
                "pcs ${model.quantityOfItem.toString()}"

            holder.searchFragmentPriceTExtView?.text =
                "${mainActivity.getString(R.string.Rs)} ${model.realTimeMrp}"

            holder.searchQuantityTextView?.text = model.quantityOfItem.toString()

            Glide.with(mainActivity.context!!)

                .load(model!!.imageSrcOfVariants[0].imageUrl).into(holder.searchFragImageView!!)



        }

    }

    inner class MainVieModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null

        var textView: TextView? = null

        var linearLayout: ConstraintLayout? = null

        var searchFragImageView: ImageView? = null

        var searchFragmentitemNameTextView: TextView? = null

        var searchFragmentnumberOfPiecesTextView: TextView? = null

        var searchFragmentPriceTExtView: TextView? = null

        var searchFragmentAddToCart: Button? = null

        var searchAddItemsQunatityImageView: ImageView? = null

        var searchRemoveItemsQuantityImageView: ImageView? = null

        var searchQuantityTextView: TextView? = null

        var addRemoveLinearLayout: LinearLayout? = null

        var cardview:CardView?=null

        init {
            Log.d("clicked", itemViewType.toString())

            if (viewType == Constants.SEARCH_FRAG_SCROLL_TYPE) {

                imageView = itemView.findViewById(R.id.HorizontalScrollItemNameImageView)

                textView = itemView.findViewById(R.id.HorizontalScrollItemNameTextView)

                linearLayout = itemView.findViewById(R.id.horizontalitemNameLinearLayout)

                linearLayout?.setOnClickListener {

                    Log.d("clicked", "yess")

                    mainActivity.activity!!.supportFragmentManager.beginTransaction()

                        .replace(R.id.container, CategoryEachViewPagerFragment(currentList[absoluteAdapterPosition],{}))
                        .addToBackStack(null)

                        .commit()
                }
            } else {

                searchFragImageView = itemView.findViewById(R.id.searchfragmentImageview)

                searchFragmentAddToCart = itemView.findViewById(R.id.searchfragmentaddtocart)

                searchFragmentPriceTExtView = itemView.findViewById(R.id.searchItemPriceTextview)

                searchFragmentitemNameTextView = itemView.findViewById(R.id.searchitemNametextView)

                cardview=itemView.findViewById(R.id.search_result_rowlayoutcardview)

                cardview!!.setOnClickListener { mainActivity.activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container,ProductFragment(currentList[absoluteAdapterPosition])).addToBackStack(null)
                    .commit()}

                addRemoveLinearLayout = itemView.findViewById(R.id.searchaddremovelinear)

                searchFragmentnumberOfPiecesTextView =

                    itemView.findViewById(R.id.searchItemPiecesTextView)

                searchAddItemsQunatityImageView =
                    itemView.findViewById(R.id.searchadditemsImageButton)

                searchRemoveItemsQuantityImageView =
                    itemView.findViewById(R.id.searchremoveitemsImageButton)

                searchQuantityTextView = itemView.findViewById(R.id.searchitemquantitiytextview)


                searchFragmentAddToCart!!.visibility = View.GONE

                addRemoveLinearLayout!!.visibility = View.GONE

                searchFragmentAddToCart!!.setOnClickListener {
                    currentList[adapterPosition].quantityOfItem++

                    searchFragmentAddToCart!!.visibility = View.GONE

                    addRemoveLinearLayout!!.visibility = View.VISIBLE

                    if (ApplicationClass.menucategorylist?.contains(currentList[adapterPosition])!!)
                        ApplicationClass.menucategorylist!!.find {
                            currentList[adapterPosition].groupId == it.groupId && currentList[adapterPosition].id == it.id
                        }!!.quantityOfItem = currentList[adapterPosition].quantityOfItem
                    else (ApplicationClass.menucategorylist as MutableList).add(currentList[adapterPosition])
                    notifyItemChanged(adapterPosition)
                    (mainActivity as SearchFragment).showOrHideItemCountIndicator()
                }






                searchAddItemsQunatityImageView!!.setOnClickListener {
                    var modelClass = currentList[adapterPosition]
                    modelClass.quantityOfItem++
                    if (ApplicationClass.menucategorylist?.contains(modelClass)!!)
                        ApplicationClass.menucategorylist!!.find {
                            modelClass.groupId == it.groupId && modelClass.id == it.id
                        }!!.quantityOfItem = modelClass.quantityOfItem
                    else (ApplicationClass.menucategorylist as MutableList).add(modelClass)
                    notifyItemChanged(adapterPosition)
                    (mainActivity as SearchFragment).showOrHideItemCountIndicator()
                }







                searchRemoveItemsQuantityImageView!!.setOnClickListener {
                    if (currentList[adapterPosition].quantityOfItem == 0) {
                        searchFragmentAddToCart!!.visibility = View.VISIBLE
                        addRemoveLinearLayout!!.visibility = View.GONE
                        if (ApplicationClass.menucategorylist!!.contains(currentList[adapterPosition]))
                            (ApplicationClass.menucategorylist as MutableList).remove(currentList[adapterPosition])
                    } else {
                        currentList[adapterPosition].quantityOfItem--
                        if (ApplicationClass.menucategorylist!!.contains(currentList[adapterPosition]))
                            ApplicationClass.menucategorylist?.find {
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






