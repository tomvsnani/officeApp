package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.Adapters.ViewPagerAdapter
import com.example.myfirstofficeappecommerce.CartFragment
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment(var list: LinkedHashMap<String, List<CategoriesModelClass>>?) :
    Fragment() {


    var viewPager2: ViewPager2? = null
    var tablayout: TabLayout? = null;
    var categoryNames: List<String>? = null
    var toolbar: Toolbar? = null
    var selectedItemsList: MutableList<CategoriesModelClass> = ArrayList()
    var cate: CategoriesModelClass? = null
    var selectedItemDisplayCardView: CardView? = null
    var itemSelectedCountTextView: TextView? = null
    var checkoutButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        var view: View = inflater.inflate(R.layout.fragment_categories, container, false)
        categoryNames = list?.keys?.toList()
        toolbar = view.findViewById(R.id.categoriesFragmenttoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        viewPager2 = view.findViewById(R.id.viewpagerincategory)
        selectedItemDisplayCardView = view.findViewById(R.id.itemsselectedindicatorcardview)
        itemSelectedCountTextView = view.findViewById(R.id.numberOfItemSelectedTextView)
        checkoutButton = view.findViewById(R.id.checkoutButton)
        if (selectedItemsList.size > 0)
            selectedItemDisplayCardView!!.visibility = View.VISIBLE
        else
            selectedItemDisplayCardView!!.visibility = View.INVISIBLE
        itemSelectedCountTextView!!.text = selectedItemsList.size.toString()
        checkoutButton!!.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, CartFragment(selectedItemsList)).addToBackStack(null)
                .commit()
        }
        (viewPager2 as ViewPager2).adapter = CategoryViewPagerAdapter(
            this, list!!
        ) { categoriesModelClass ->
            if (selectedItemsList.contains(categoriesModelClass)) {
                selectedItemsList.remove(categoriesModelClass)
                categoriesModelClass.quantityOfItem--
            } else {
                categoriesModelClass.itemQueueNumber=selectedItemsList.size
                selectedItemsList.add(categoriesModelClass)

            }

            if (selectedItemsList.size > 0)
                selectedItemDisplayCardView!!.visibility = View.VISIBLE
            else
                selectedItemDisplayCardView!!.visibility = View.INVISIBLE
            itemSelectedCountTextView!!.text = selectedItemsList.size.toString()
        }
        tablayout = view.findViewById(R.id.tablayoutincategory)

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

            tab.text = (categoryNames as List<String>)[i]
        }.attach()
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_menu) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())?.addToBackStack(null)
                ?.commit()
        }
        return super.onOptionsItemSelected(item)
    }
}
