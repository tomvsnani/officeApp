package com.example.myfirstofficeappecommerce.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass

class CategoryViewPagerAdapter(
    fragment: CategoriesFragment,
    var map: LinkedHashMap<String, List<CategoriesModelClass>>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return map.size
    }

    override fun createFragment(position: Int): Fragment {
     return CategoriesEachViewPagerFragment(map.get(map.keys.toTypedArray().get(position)))
    }
}