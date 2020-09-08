package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.test_resouce_file.*


class ProductFragment (var modelClass: CategoriesModelClass): Fragment() {


    var viewPager2: ViewPager2? = null
    var tablayout: TabLayout? = null;
    var toolbar: Toolbar? = null
    var itemNameTextView: TextView? = null
    var itemPriceTextView: TextView? = null
    var itemTotalDescriptionTextView: TextView? = null
    var itemQuantitiyTextView: TextView? = null
    var itemMessagesImageView: ImageView? = null
    var itemShareImageView: ImageView? = null
    var itemAddImageView:ImageView?=null
    var itemremoveImageView:ImageView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_product, container, false)
        toolbar = view.findViewById(R.id.productToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewPager2 = view.findViewById(R.id.productviewpager)
        (viewPager2 as ViewPager2).adapter =
            HorizontalScrollViewPagerAdapter(
                this,
                CategoriesDataProvider().getListDataForHorizontalScroll()
            )
        tablayout = view.findViewById(R.id.producttablayout)
        itemNameTextView = view.findViewById(R.id.productItemNameTextView)
        itemPriceTextView = view.findViewById(R.id.productPagePriceTextView)
        itemQuantitiyTextView = view.findViewById(R.id.productitemquantitiytextview)
        itemTotalDescriptionTextView = view.findViewById(R.id.productpageDescriptionTextView)
        itemMessagesImageView=view.findViewById(R.id.productPageMessagesImageView)
        itemShareImageView=view.findViewById(R.id.productPageShareTImageView)
        itemAddImageView=view.findViewById(R.id.productadditemsImageButton)
        itemremoveImageView=view.findViewById(R.id.productremoveitemsImageButton)

       itemAddImageView!!.setOnClickListener {  }
        itemremoveImageView!!.setOnClickListener {  }

        itemNameTextView!!.text=modelClass.itemName
        itemTotalDescriptionTextView!!.text=modelClass.itemDescriptionText
        itemQuantitiyTextView!!.text=modelClass.quantityOfItem.toString()
        itemPriceTextView!!.text=modelClass.realTimeMrp

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu, menu)

        var item: MenuItem = menu.findItem(R.id.cartmenu)
        item.actionView.findViewById<ImageView>(R.id.cartmenuitem).setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    CartFragment(
                        ApplicationClass.selectedItemsList
                    )
                )
                .addToBackStack(null)
                .commit()

        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.search_menu) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())?.addToBackStack(null)
                ?.commit()
            return true
        }
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}