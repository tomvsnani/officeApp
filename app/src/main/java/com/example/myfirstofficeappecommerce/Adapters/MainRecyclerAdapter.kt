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
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment

class MainRecyclerAdapter(var mainActivity: MainActivity,var map:LinkedHashMap<String,List<CategoriesModelClass>>?) :
    ListAdapter<ModelClass, MainRecyclerAdapter.MainVieModel>(
        ModelClass.diffUtil
    ) {
    var list:List<CategoriesModelClass>?=null;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVieModel {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontalscrollitemnamerowlay, parent, false)
        return MainVieModel(view)
    }

    override fun onBindViewHolder(holder: MainVieModel, position: Int) {
        val model: ModelClass = currentList[position]
        holder.textView.text = model.title
    }

    inner class MainVieModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.HorizontalScrollItemNameImageView)
        var textView: TextView = itemView.findViewById(R.id.HorizontalScrollItemNameTextView)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.horizontalitemNameLinearLayout)

        init {
            linearLayout.setOnClickListener {
                Log.d("clicked","yess")

                mainActivity.supportFragmentManager.beginTransaction().replace(R.id.container,CategoriesFragment(map)).addToBackStack(null)
                    .commit()
            }
        }


    }


}



