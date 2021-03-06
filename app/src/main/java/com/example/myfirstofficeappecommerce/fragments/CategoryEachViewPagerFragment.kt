package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.CategoriesEachRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R


class CategoryEachViewPagerFragment(var get: List<CategoriesModelClass>?,var callback:(Pair<String,CategoriesModelClass>)->Unit) : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_category_each_viewpager,
            container,
            false
        )
        recyclerView = view.findViewById(R.id.categoriesRecyclerview)
        adapterr = CategoriesEachRecyclerAdapter(callback )
        (recyclerView as RecyclerView).layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        recyclerView!!.itemAnimator=null
        (adapterr as CategoriesEachRecyclerAdapter).submitList(get as MutableList<CategoriesModelClass>?)
        return view
    }


}