package com.example.myfirstofficeappecommerce.fragments

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
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
    var variantList: List<VariantsModelClass>? = modelClass.variantsList!!.toList()


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        selectedVariant = ApplicationClass.selectedVariantList?.find { it.isSelected }?.copy() ?: modelClass.variantsList!![0].copy()
        var view: View = inflater.inflate(R.layout.fragment_product_layout_2, container, false)
        initializeViews(view)
        (activity as MainActivity).lockDrawer()
        toolbar = view.findViewById(R.id.productToolbar)
        setHasOptionsMenu(true)
        for (selectedVariant in variantList!!)
            Log.d(
                "selecteddcolorr",
                "${selectedVariant!!.color} ${selectedVariant!!.size} ${selectedVariant!!.id}"
            )
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




        colorRecyclerAdapter = ProductColorRecyclerViewAdapter(this) { colorr ->

            val isVariantAvailable=variantList!!.find { it.color == colorr && it.size == selectedVariant!!.size }

            if(isVariantAvailable!=null) {
                val isVariantavailableInApplicationClass=ApplicationClass.selectedVariantList!!.find { it.id == isVariantAvailable.id }



                if (isVariantavailableInApplicationClass == null) {
                    selectedVariant = isVariantAvailable.copy(quantityOfItem = 0)
                    addTocartButton!!.visibility = View.VISIBLE
                    addOrRemoveItemsLinear!!.visibility = View.GONE
                }
                else{
                    selectedVariant = isVariantavailableInApplicationClass.copy()
                    addTocartButton!!.visibility = View.GONE
                    addOrRemoveItemsLinear!!.visibility = View.VISIBLE
                }
            }
            else
                Toast.makeText(context,"selected color or size is not available",Toast.LENGTH_SHORT).show()

            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

            itemPriceTextView!!.text =
                "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant!!.price}"


            Log.d(
                "selecteddcolor",
                "${selectedVariant!!.color} ${selectedVariant!!.size} ${selectedVariant!!.id}"
            )
        }

        colorRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        colorRecyclerView!!.adapter = colorRecyclerAdapter

        sizeRecyclerViewAdapter =
            ProductSizeRecyclerViewAdapter { sizee ->

                val isVariantAvailable=variantList!!.find { it.color == selectedVariant!!.color && it.size == sizee }


                if(isVariantAvailable!=null) {

                    val isVariantavailableInApplicationClass=ApplicationClass.selectedVariantList!!.find { it.id == isVariantAvailable.id }

                    if (isVariantavailableInApplicationClass== null) {

                        selectedVariant = isVariantAvailable.copy( quantityOfItem = 0)

                        addTocartButton!!.visibility = View.VISIBLE
                        addOrRemoveItemsLinear!!.visibility = View.GONE
                    }
                    else {
                        selectedVariant=isVariantavailableInApplicationClass.copy()
                        addTocartButton!!.visibility = View.GONE
                        addOrRemoveItemsLinear!!.visibility = View.VISIBLE
                    }
                }
                else
                    Toast.makeText(context,"selected color or size is not available",Toast.LENGTH_SHORT).show()


                itemPriceTextView!!.text =
                    "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant!!.price}"

                itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

                Log.d("selecteditems",ApplicationClass.selectedVariantList.toString())

                Log.d(
                    "selecteddsize",
                    "${selectedVariant!!.color} ${selectedVariant!!.size} ${selectedVariant!!.id}"
                )
            }


        sizeRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        sizeRecyclerView!!.adapter = sizeRecyclerViewAdapter

        colorRecyclerAdapter!!.submitList(modelClass.variantsList!!.distinctBy { it.color }
            .apply { find { it.id == selectedVariant!!.id }?.isSelected = true } as MutableList<VariantsModelClass>)

        sizeRecyclerViewAdapter!!.submitList(modelClass.variantsList!!.distinctBy { it.size }
            .apply { find { it.id == selectedVariant!!.id }?.isSelected = true }
            .asReversed().sortedBy { it.size } as MutableList<VariantsModelClass>)


        itemShareImageView!!.setOnClickListener {
            var s = modelClass.itemName
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html";
            intent.putExtra(Intent.EXTRA_TEXT, s)
            activity!!.startActivity(intent)
        }



        if (selectedVariant!!.quantityOfItem > 0) {
            addTocartButton!!.visibility = View.GONE
            addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            itemQuantitiyTextView!!.text = Utils.getItemCount()
        }

        itemAddImageView!!.setOnClickListener {
//            modelClass.quantityOfItem++
            selectedVariant!!.quantityOfItem++
            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
            selectedVariant!!.isSelected=true
//            ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =
//                modelClass.quantityOfItem

            ApplicationClass.selectedVariantList?.find {
                it.id == selectedVariant!!.id
                        && it.parentProductId == selectedVariant!!.parentProductId
            }!!.quantityOfItem =
                selectedVariant!!.quantityOfItem


            Log.d("selecteditems",ApplicationClass.selectedVariantList.toString())
            showOrHideItemCountIndicator()
        }




        addTocartButton!!.setOnClickListener {


//            modelClass.id = selectedVariant!!.id!!
//            modelClass.groupId = selectedVariant!!.parentProductId!!
//            var modelClassTemp =
//                ApplicationClass.selectedItemsList?.apply {
//                    filter { categoriesModelClass ->
//                        categoriesModelClass.variantsList!!.find { it.id == modelClass.id && it.parentProductId == modelClass.groupId }
//                        return@filter true
//                    }
//                }


            //adding product model to selected items
            if (ApplicationClass.selectedVariantList!!.find { it.id == selectedVariant!!.id } == null) {
//                selectedVariant =
//                    variantList!!.filter { it.color == selectedVariant!!.color && it.size == selectedVariant!!.size }[0]
                selectedVariant!!.quantityOfItem++
                selectedVariant!!.isSelected=true
                ApplicationClass.selectedVariantList!!.add(selectedVariant!!)
//                modelClass.quantityOfItem++
//                (ApplicationClass.selectedItemsList as MutableList).add(modelClass)

                addTocartButton!!.visibility = View.GONE
                addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            }
            Log.d("selecteditems",ApplicationClass.selectedVariantList.toString())

            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
            showOrHideItemCountIndicator()


        }




        itemremoveImageView!!.setOnClickListener {
            if (selectedVariant!!.quantityOfItem > 0) {
                //   modelClass.quantityOfItem--
                selectedVariant!!.quantityOfItem--
//                ApplicationClass.selectedItemsList?.find { it.id == modelClass.id && it.groupId == modelClass.groupId }!!.quantityOfItem =
//                    modelClass.quantityOfItem
                ApplicationClass.selectedVariantList?.find {
                    it.id == selectedVariant!!.id

                }!!.quantityOfItem =
                    selectedVariant!!.quantityOfItem
                Log.d("selecteditems",ApplicationClass.selectedVariantList.toString())
            }


            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

            if (selectedVariant!!.quantityOfItem == 0 && ApplicationClass.selectedVariantList?.find { it.id == selectedVariant!!.id } != null) {
                addTocartButton!!.visibility = View.VISIBLE
                addOrRemoveItemsLinear!!.visibility = View.GONE

//                (ApplicationClass.selectedItemsList as MutableList).remove(modelClass)
                (ApplicationClass.selectedVariantList)!!.remove(selectedVariant!!)
            }

            showOrHideItemCountIndicator()

        }

        itemNameTextView!!.text = modelClass.itemName

        itemTotalDescriptionTextView!!.text = modelClass.itemDescriptionText


        itemPriceTextView!!.text =
            "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant!!.price}"

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
                        ApplicationClass.selectedVariantList
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