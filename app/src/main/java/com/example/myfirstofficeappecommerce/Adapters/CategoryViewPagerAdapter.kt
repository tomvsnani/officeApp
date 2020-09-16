package com.example.myfirstofficeappecommerce.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.fragments.CategoriesFragment
import com.example.myfirstofficeappecommerce.fragments.CategoryEachViewPagerFragment

class CategoryViewPagerAdapter(
    fragment: CategoriesFragment,
    var map: List<CategoriesModelClass>,
    var callback: () -> Unit
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {

        return map.size
    }

    override fun createFragment(position: Int): Fragment {
        return CategoryEachViewPagerFragment(map[position],callback)
        
    }
}