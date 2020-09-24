package com.example.myfirstofficeappecommerce.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.CategoriesEachViewpagerGridRowlayoutBinding
import com.example.myfirstofficeappecommerce.databinding.DifferentProductsCategoriesRowlayoutBinding
import com.example.myfirstofficeappecommerce.fragments.ProductFragment
import kotlinx.coroutines.*

class DifferentProductsCategoriesRecyclerViewAdapter(

) :
    ListAdapter<CategoriesModelClass, DifferentProductsCategoriesRecyclerViewAdapter.BannerViewHolder>(
        CategoriesModelClass.diffUtil
    ) {


    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: DifferentProductsCategoriesRowlayoutBinding =
            DifferentProductsCategoriesRowlayoutBinding.bind(itemView)
        var imageview = binding.differentproductCategoryRowlayoutimageview

        init {
            imageview.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.different_products_categories_rowlayout, parent, false)
        return BannerViewHolder(view)
    }


    override fun submitList(list: MutableList<CategoriesModelClass>?) {
        super.submitList(list?.toList())
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 4
    }


}