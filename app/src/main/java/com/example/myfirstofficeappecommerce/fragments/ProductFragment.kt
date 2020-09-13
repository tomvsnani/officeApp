package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.test_resouce_file.*


class ProductFragment(var modelClass: CategoriesModelClass) : Fragment() {


    var viewPager2: ViewPager2? = null
    var tablayout: TabLayout? = null;
    var toolbar: Toolbar? = null
    var itemNameTextView: TextView? = null
    var itemPriceTextView: TextView? = null
    var itemTotalDescriptionTextView: TextView? = null
    var itemQuantitiyTextView: TextView? = null
    var itemMessagesImageView: ImageView? = null
    var itemShareImageView: ImageView? = null
    var itemAddImageView: ImageView? = null
    var itemremoveImageView: ImageView? = null
    var menu: Menu? = null
    var addTocartButton: Button? = null
    var addOrRemoveItemsLinear: LinearLayout? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_product, container, false)
        (activity as MainActivity).lockDrawer()

        toolbar = view.findViewById(R.id.productToolbar)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager2 = view.findViewById(R.id.productviewpager)

        (viewPager2 as ViewPager2).adapter =

            HorizontalScrollViewPagerAdapter(
                this,
                CategoriesDataProvider.getListDataForHorizontalScroll()
            )
        tablayout = view.findViewById(R.id.producttablayout)

        itemNameTextView = view.findViewById(R.id.productItemNameTextView)

        itemPriceTextView = view.findViewById(R.id.productPagePriceTextView)

        itemQuantitiyTextView = view.findViewById(R.id.productitemquantitiytextview)

        itemTotalDescriptionTextView = view.findViewById(R.id.productpageDescriptionTextView)

        itemMessagesImageView = view.findViewById(R.id.productPageMessagesImageView)

        itemShareImageView = view.findViewById(R.id.productPageShareTImageView)

        itemAddImageView = view.findViewById(R.id.productadditemsImageButton)

        itemremoveImageView = view.findViewById(R.id.productremoveitemsImageButton)

        addTocartButton = view.findViewById(R.id.productaddToCartButton)

        addOrRemoveItemsLinear = view.findViewById(R.id.productaddorremoveitemslinearlayout)

        if (modelClass.quantityOfItem > 0) {
            addTocartButton!!.visibility = View.GONE
            addOrRemoveItemsLinear!!.visibility = View.VISIBLE
        }



        itemAddImageView!!.setOnClickListener {
            modelClass.quantityOfItem++
            itemQuantitiyTextView!!.text = modelClass.quantityOfItem.toString()



            ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =

                modelClass.quantityOfItem

            showOrHideItemCountIndicator()
        }

        addTocartButton!!.setOnClickListener {
            var modelClassTemp =
                ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }
            if (modelClassTemp == null) {
                modelClass.quantityOfItem++
                (ApplicationClass.selectedItemsList as MutableList).add(modelClass)
                addTocartButton!!.visibility = View.GONE
                addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            }
            itemQuantitiyTextView!!.text = modelClass.quantityOfItem.toString()
            showOrHideItemCountIndicator()
        }




        itemremoveImageView!!.setOnClickListener {
            if (modelClass.quantityOfItem > 0) {
                modelClass.quantityOfItem--
                ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =
                    modelClass.quantityOfItem
            }
            itemQuantitiyTextView!!.text = modelClass.quantityOfItem.toString()

            if (modelClass.quantityOfItem == 0 && ApplicationClass.selectedItemsList?.contains(
                    modelClass
                )!!
            ) {
                addTocartButton!!.visibility = View.VISIBLE
                addOrRemoveItemsLinear!!.visibility = View.GONE

                (ApplicationClass.selectedItemsList as MutableList).remove(modelClass)
            }

            showOrHideItemCountIndicator()

        }

        itemNameTextView!!.text = modelClass.itemName

        itemTotalDescriptionTextView!!.text = modelClass.itemDescriptionText

        itemQuantitiyTextView!!.text = modelClass.quantityOfItem.toString()

        itemPriceTextView!!.text = "${activity!!.getString(R.string.Rs)} ${modelClass.realTimeMrp}"

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragmentmenu, menu)
        this.menu = menu
        var item: MenuItem = menu.findItem(R.id.cartmenu)
        item.actionView.findViewById<ImageView>(R.id.cartmenuitem).setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    CartFragment(
                        ApplicationClass.selectedItemsList
                    )
                )
                .addToBackStack(null)
                .commit()


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

            activity?.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun showOrHideItemCountIndicator() {

        var itemCount = Utils.getItemCount()

        if (itemCount.toInt() > 0) {

            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)
                .apply {
                    text = itemCount.toString()
                    visibility = View.VISIBLE
                }

        } else {
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu).visibility =
                View.INVISIBLE


        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0 until menu.size())
            if (menu.getItem(i).itemId != R.id.cartmenu)
                menu.getItem(i).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroyView() {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        super.onDestroyView()
    }
}