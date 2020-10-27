package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.SubCategoryRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.CollectionItem
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentSubCategoryBinding


class SubCategoryFragment(var collectionItems: List<CollectionItem>) : Fragment() {
    var binding: FragmentSubCategoryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_sub_category, container, false)
        var adapter = SubCategoryRecyclerAdapter(this)
        binding = FragmentSubCategoryBinding.bind(view)
        binding!!.subcategoriesrecyclerview.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding!!.subcategoriesrecyclerview.adapter = adapter
        adapter.submitList(collectionItems)
        return view
    }

}