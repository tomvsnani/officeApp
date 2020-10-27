package com.example.myfirstofficeappecommerce.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.Models.CollectionItem
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.SubCategoriesRowLayoutBinding
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment
import com.example.myfirstofficeappecommerce.fragments.SubCategoryFragment

class SubCategoryRecyclerAdapter(var fragment: SubCategoryFragment) :
    RecyclerView.Adapter<SubCategoryRecyclerAdapter.SubCategoryViewHolder>() {

    var list: List<CollectionItem> = ArrayList()

    inner class SubCategoryViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var binding = SubCategoriesRowLayoutBinding.bind(view)

        init {
            binding!!.subcategoriesrowlayout.setOnClickListener {
                fragment.activity!!.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        CategoryEachViewPagerFragment(CategoriesModelClass(id = list[absoluteAdapterPosition].collection_id)) {}
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_categories_row_layout, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        var item = list[position]
        holder.binding.subcategoriesrowlayout.text = item.collection_name
        Glide.with(fragment).load(item.collection_image).centerCrop()
            .into(holder.binding.subcategoryimagview)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(list:List<CollectionItem>){
        this.list=list
        notifyDataSetChanged()
    }
}