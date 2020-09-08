package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment
import com.example.myfirstofficeappecommerce.fragments.ProductFragment

class CategoriesEachRecyclerAdapter(
    callback: (Pair<String, CategoriesModelClass>) -> Unit,
    var categoryEachViewPagerFragment: CategoryEachViewPagerFragment
) :
    ListAdapter<CategoriesModelClass, CategoriesEachRecyclerAdapter.CategoryViewHolder>(
        CategoriesModelClass.diffUtil
    ) {
    var mutableselectedItemsList: MutableLiveData<Pair<String,CategoriesModelClass>> = MutableLiveData()

    init {
        mutableselectedItemsList.observeForever(Observer {

           if(it.first=="add") {
               it.second.quantityOfItem++

           }
            else
               it.second.quantityOfItem--

            Log.d("modelclass",it.second.quantityOfItem.toString())
            notifyItemChanged(currentList.indexOf(it.second))
            callback(it)
        })
    }


    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ItemImageView)
        val itemName: TextView = itemView.findViewById(R.id.ItemNameTextView)
        val itemDescription: TextView = itemView.findViewById(R.id.ItemDescriptionTextView)
        val itemGrossweight: TextView = itemView.findViewById(R.id.GrossWeightTextView)
        val itemNetWeight: TextView = itemView.findViewById(R.id.NetWeightTextView)
        val realmrp: TextView = itemView.findViewById(R.id.originalPriceTextView)
        val addToCart: Button = itemView.findViewById(R.id.addToCartButton)
        val addOrRemoveItemLinearLayout: LinearLayout =
            itemView.findViewById(R.id.addorremoveitemslinearlayout)
        val removeItemsImageButton: ImageButton = itemView.findViewById(R.id.removeitemsImageButton)
        val addItemsImageButton: ImageButton = itemView.findViewById(R.id.additemsImageButton)
        val quantityOfItemAddaedToCartTextView: TextView =
            itemView.findViewById(R.id.cartitemquantitiytextview)


        init {
            addToCart.setOnClickListener {
                mutableselectedItemsList.value = Pair("add",currentList[adapterPosition])

            }

            removeItemsImageButton.setOnClickListener {
                mutableselectedItemsList.value = Pair("remove",currentList[adapterPosition])
            }

            addItemsImageButton.setOnClickListener {
                mutableselectedItemsList.value = Pair("add",currentList[adapterPosition])
            }

            itemImage.setOnClickListener{categoryEachViewPagerFragment.activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container,ProductFragment(currentList[adapterPosition])).addToBackStack(null).commit()}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_each_viewpager_row_layout, parent, false)
        return CategoryViewHolder(view)
    }


    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        var modelClass = currentList[position]
        Log.d("finding",modelClass.itemName+" "+modelClass.quantityOfItem)
        if (modelClass.quantityOfItem > 0) {
            holder.addToCart.visibility = View.GONE
            holder.addOrRemoveItemLinearLayout.visibility = View.VISIBLE
            holder.quantityOfItemAddaedToCartTextView.text = modelClass.quantityOfItem.toString()
        } else {
            holder.addToCart.visibility = View.VISIBLE
            holder.addOrRemoveItemLinearLayout.visibility = View.GONE
        }
        holder.itemDescription.text = modelClass.itemDescriptionText
        holder.realmrp.text = modelClass.realTimeMrp
        holder.itemGrossweight.text = modelClass.itemGrossWeight
        holder.itemName.text = modelClass.itemName
        holder.itemNetWeight.text = modelClass.itemNetWeight
    }


}