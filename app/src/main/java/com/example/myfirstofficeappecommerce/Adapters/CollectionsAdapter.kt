package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.MainFragment

class CollectionsAdapter(
    var mainActivity: MainFragment
) :
    ListAdapter<CategoriesModelClass, CollectionsAdapter.MainVieModel>(
        CategoriesModelClass.diffUtil
    ) {


    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
        Log.d("remotead", list.toString())
        notifyDataSetChanged()
    }


    var list: List<CategoriesModelClass>? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {


        val view: View = LayoutInflater.from(parent.context)

            .inflate(R.layout.collections_row_layout, parent, false)

        return MainVieModel(view)
    }


    override fun onBindViewHolder(holder: MainVieModel, position: Int) {


        val model: CategoriesModelClass = currentList[position]


        holder.textView?.text = model.itemName

        Glide.with(mainActivity).load(model.imageUrl).circleCrop().into(holder.imageView!!)


    }

    inner class MainVieModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null

        var textView: TextView? = null

        var linearLayout: LinearLayout? = null


        init {
            imageView = itemView.findViewById(R.id.HorizontalScrollItemNameImageView)

            textView = itemView.findViewById(R.id.HorizontalScrollItemNameTextView)

            linearLayout = itemView.findViewById(R.id.horizontalitemNameLinearLayout)

            linearLayout?.setOnClickListener {

                Log.d("clicked", "yess")
                ApplicationClass.selectedTab = adapterPosition
                mainActivity.activity!!.supportFragmentManager.beginTransaction()

                    .replace(R.id.container, CategoriesFragment(currentList, adapterPosition))
                    .addToBackStack(null)

                    .commit()
            }


        }

    }


}






