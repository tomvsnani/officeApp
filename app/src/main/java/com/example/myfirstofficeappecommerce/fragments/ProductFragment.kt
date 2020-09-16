package com.example.myfirstofficeappecommerce.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Adapters.ProductColorRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Adapters.ProductSizeRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ProductColorModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>? = null
    var colorRecyclerView: RecyclerView? = null
    var sizeRecyclerView: RecyclerView? = null
    var colorRecyclerAdapter: ProductColorRecyclerViewAdapter? = null
    var sizeRecyclerViewAdapter: ProductSizeRecyclerViewAdapter? = null
    var selectedVariant: VariantsModelClass? = null
    var variantList:List<VariantsModelClass>?=modelClass.variantsList!!.toList()


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        selectedVariant=modelClass.variantsList?.getOrNull(0)!!.copy()

        var view: View = inflater.inflate(R.layout.fragment_product_layout_2, container, false)

        (activity as MainActivity).lockDrawer()

        toolbar = view.findViewById(R.id.productToolbar)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager2 = view.findViewById(R.id.productviewpager)

        (viewPager2 as ViewPager2).adapter =

            HorizontalScrollViewPagerAdapter(
                this,
                modelClass.imageSrc
            )

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomsheet))

        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0.9f) {
                    toolbar!!.visibility = View.INVISIBLE
                } else
                    toolbar!!.visibility = View.VISIBLE
            }
        })

        initializeViews(view)


        colorRecyclerAdapter = ProductColorRecyclerViewAdapter(this) { colorr ->
            selectedVariant!!.color = colorr
            selectedVariant!!.id=
                variantList!!.find { it.color == selectedVariant!!.color && it.size == selectedVariant!!.size }?.id

        }

        colorRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        colorRecyclerView!!.adapter = colorRecyclerAdapter

        sizeRecyclerViewAdapter =
            ProductSizeRecyclerViewAdapter { sizee ->
                selectedVariant!!.size = sizee
                selectedVariant!!.id =
                    variantList!!.find { it.color == selectedVariant!!.color && it.size == selectedVariant!!.size }?.id

            }


        sizeRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        sizeRecyclerView!!.adapter = sizeRecyclerViewAdapter

        colorRecyclerAdapter!!.submitList(modelClass.variantsList!!.distinctBy { it.color }
            .apply { find { it.id==selectedVariant!!.id }?.isSelected=true })

        sizeRecyclerViewAdapter!!.submitList(modelClass.variantsList!!.distinctBy { it.size }
            .apply { find { it.id==selectedVariant!!.id }?.isSelected=true }
            .asReversed().sortedBy { it.size })


        itemShareImageView!!.setOnClickListener {
            var s = modelClass.itemName
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html";
            intent.putExtra(Intent.EXTRA_TEXT, s)
            activity!!.startActivity(intent)
        }

      for(variant in modelClass.variantsList!!){
       if(  ApplicationClass.selectedItemsList!!.find { it.id==variant.id && it.groupId==variant.parentProductId }!=null) {
           addTocartButton!!.visibility = View.GONE
           addOrRemoveItemsLinear!!.visibility = View.VISIBLE
       }
      }


        itemAddImageView!!.setOnClickListener {
            modelClass.quantityOfItem++
            itemQuantitiyTextView!!.text = modelClass.quantityOfItem.toString()



            ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =

                modelClass.quantityOfItem

            Log.d("responsee",ApplicationClass.selectedItemsList.toString())

            showOrHideItemCountIndicator()
        }

        addTocartButton!!.setOnClickListener {
            selectedVariant =
                modelClass.variantsList!!.filter { it.color == selectedVariant!!.color && it.size == selectedVariant!!.size }[0]

            modelClass.id = selectedVariant!!.id!!
            modelClass.groupId = selectedVariant!!.parentProductId!!
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

            Log.d("responsee",ApplicationClass.selectedItemsList.toString())
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

        itemPriceTextView!!.text =
            "MRP : ${activity!!.getString(R.string.Rs)} ${modelClass.realTimeMrp}"

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()
        return view
    }

    private fun initializeViews(view: View) {
        colorRecyclerView = view.findViewById(R.id.productcolorRecyclerView)

        sizeRecyclerView = view.findViewById(R.id.productsizeRecyclerview)

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