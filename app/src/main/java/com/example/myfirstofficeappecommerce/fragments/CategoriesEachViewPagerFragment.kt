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


class CategoriesEachViewPagerFragment(var get: List<CategoriesModelClass>?) : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapterr: CategoriesEachRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_categories_each_view_pager_fragment,
            container,
            false
        )
        recyclerView = view.findViewById(R.id.categoriesRecyclerview)
        adapterr = CategoriesEachRecyclerAdapter()
        (recyclerView as RecyclerView).layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (recyclerView as RecyclerView).adapter = adapterr
        (adapterr as CategoriesEachRecyclerAdapter).submitList(get)


        return view
    }


}