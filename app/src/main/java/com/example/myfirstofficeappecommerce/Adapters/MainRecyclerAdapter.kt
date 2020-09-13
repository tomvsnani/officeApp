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

class MainRecyclerAdapter(
    var mainActivity: MainFragment, var map: LinkedHashMap<String,
            List<CategoriesModelClass>>?
) :
    ListAdapter<CategoriesModelClass, MainRecyclerAdapter.MainVieModel>(
        CategoriesModelClass.diffUtil
    ) {
    init {
        var list: LinkedList<CategoriesModelClass> = LinkedList()
        map!!.keys.toList().forEach { list.add(CategoriesModelClass(itemName = it)) }
        Log.d("remotead","before")

        CategoriesDataProvider.mutableCollectionList.observeForever{t: MutableList<CategoriesModelClass>? ->
            submitList(t)
        }

    }

    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
        Log.d("remotead",list.toString())
        notifyDataSetChanged() }


    var list: List<CategoriesModelClass>? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {



        val view: View = LayoutInflater.from(parent.context)

            .inflate(R.layout.horizontalscrollitemnamerowlay, parent, false)

        return MainVieModel(view)
    }




    override fun onBindViewHolder(holder: MainVieModel, position: Int) {


        val model: CategoriesModelClass = currentList[position]


        holder.textView?.text = model.itemName


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

                mainActivity.activity!!.supportFragmentManager.beginTransaction()

                    .replace(R.id.container, CategoriesFragment(map,adapterPosition)).addToBackStack(null)

                    .commit()
            }



        }

    }


}






