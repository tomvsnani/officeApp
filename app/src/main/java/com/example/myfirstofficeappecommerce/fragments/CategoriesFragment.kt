package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.Adapters.CategoryViewPagerAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class CategoriesFragment(var list: LinkedHashMap<String, List<CategoriesModelClass>>?,var tabType:Int) :
    Fragment() {


    var viewPager2: ViewPager2? = null
    var tablayout: TabLayout? = null;
    var categoryNames: List<String>? = null
    var toolbar: Toolbar? = null
    var selectedItemsList: MutableList<CategoriesModelClass> =
        ApplicationClass.selectedItemsList as MutableList<CategoriesModelClass>
    var cate: CategoriesModelClass? = null
    var selectedItemDisplayCardView: CardView? = null
    var itemSelectedCountTextView: TextView? = null
    var checkoutButton: Button? = null
    var menu: Menu? = null
    var indexOfItemInList:Int=-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        Log.d("liscount", selectedItemsList.size.toString())
        var view: View = inflater.inflate(R.layout.fragment_categories, container, false)
        categoryNames = list?.keys?.toList()
        toolbar = view.findViewById(R.id.categoriesFragmenttoolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).actionBarToggle = ActionBarDrawerToggle(
            activity, (activity as MainActivity).drawerLayout, R.string.openDrawerLayout,
            R.string.closeDrawerLayout
        )

        ((activity as MainActivity).drawerLayout as DrawerLayout).addDrawerListener((activity as MainActivity).actionBarToggle!!)
        ((activity as MainActivity).actionBarToggle as ActionBarDrawerToggle).syncState()
        viewPager2 = view.findViewById(R.id.viewpagerincategory)
        selectedItemDisplayCardView = view.findViewById(R.id.itemsselectedindicatorcardview)
        itemSelectedCountTextView = view.findViewById(R.id.numberOfItemSelectedTextView)
        checkoutButton = view.findViewById(R.id.checkoutButton)
        checkoutButton!!.setOnClickListener {
            openCartFragment()
        }
           (viewPager2 as ViewPager2).adapter =
            CategoryViewPagerAdapter(
                this, list!!
            ) { categoriesModelClass ->
                indexOfItemInList = -1
                if (selectedItemsList.size == 0)
                    selectedItemsList.add(categoriesModelClass.second)
                selectedItemsList.filter { a ->

                     if (a.id == categoriesModelClass.second.id && a.groupId == categoriesModelClass.second.groupId) {
                        indexOfItemInList = selectedItemsList.indexOf(categoriesModelClass.second)
                        return@filter true
                    }
                    return@filter false
                }

                   if (indexOfItemInList != -1)
                    selectedItemsList[indexOfItemInList] = categoriesModelClass.second
                else
                    selectedItemsList.add(categoriesModelClass.second)
                if(categoriesModelClass.second.quantityOfItem==0)
                    selectedItemsList.removeAt(indexOfItemInList)
                ApplicationClass.selectedItemsList=selectedItemsList
                showOrHideItemCountIndicator()
            }


        tablayout = view.findViewById(R.id.tablayoutincategory)


        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

            tab.text = (categoryNames as List<String>)[i]
        }.attach()
        viewPager2!!.currentItem = tabType
        return view
    }

    private fun openCartFragment() {
           activity!!.supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                CartFragment(
                    ApplicationClass.selectedItemsList?.filter { it.quantityOfItem > 0 })
            ).addToBackStack(null)
            .commit()
    }


    private fun showOrHideItemCountIndicator() {
        if(ApplicationClass.selectedItemsList!!.isNotEmpty()){
            Log.d(
                "comparesize",
                (ApplicationClass.selectedItemsList!!).toString()
            )

            Log.d(
                "comparesize",
                (ApplicationClass.selectedItemsList!!.size).toString()
            )
        }

        var itemCount= Utils.getItemCount()
        itemSelectedCountTextView!!.text = itemCount.toString()
        if (itemCount.toInt() > 0) {
            selectedItemDisplayCardView!!.visibility = View.VISIBLE
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)
                .apply {
                    text = itemCount.toString()
                    visibility = View.VISIBLE
                }

        } else {
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu).visibility =
                View.INVISIBLE

            selectedItemDisplayCardView!!.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu, menu)
        this.menu = menu
        var item: MenuItem = menu.findItem(R.id.cartmenu)
        item.actionView.findViewById<ImageView>(R.id.cartmenuitem).setOnClickListener {
          openCartFragment()
        }
        showOrHideItemCountIndicator()
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.search_menu) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())?.addToBackStack(null)
                ?.commit()
            return true
        }
        if (item.itemId == android.R.id.home) {
            if ((activity as MainActivity).drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                (activity as MainActivity).drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                (activity as MainActivity).drawerLayout?.openDrawer(GravityCompat.START)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition= inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }
}
