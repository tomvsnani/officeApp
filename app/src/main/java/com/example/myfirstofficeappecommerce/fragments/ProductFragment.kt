package com.example.myfirstofficeappecommerce.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
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


class ProductFragment(private var modelClass: CategoriesModelClass) : Fragment() {


    private var viewPager2: ViewPager2? = null
    private var tablayout: TabLayout? = null;
    var toolbar: Toolbar? = null
    private var itemNameTextView: TextView? = null
    private var itemPriceTextView: TextView? = null
    private var itemTotalDescriptionTextView: WebView? = null
    private var itemQuantitiyTextView: TextView? = null
    private var itemMessagesImageView: ImageView? = null
    private var itemShareImageView: ImageView? = null
    private var itemAddImageView: ImageView? = null
    private var itemremoveImageView: ImageView? = null
    var menu: Menu? = null
    private var addTocartButton: Button? = null
    private var addOrRemoveItemsLinear: LinearLayout? = null
    private var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>? = null
    private var colorRecyclerView: RecyclerView? = null
    private var sizeRecyclerView: RecyclerView? = null
    private var colorRecyclerAdapter: ProductColorRecyclerViewAdapter? = null
    private var sizeRecyclerViewAdapter: ProductSizeRecyclerViewAdapter? = null
    private var selectedVariant: VariantsModelClass? = null
    private var variantList: List<VariantsModelClass>? = modelClass.variantsList!!.toList()
    private var htmlDescriptionScroll: ScrollView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (ApplicationClass.selectedVariantList?.find { it.isSelected } != null) {
            selectedVariant = ApplicationClass.selectedVariantList?.find { it.isSelected }?.copy()
            Log.d(
                "selecteddcolorinapp",
                selectedVariant!!.color.toString() + " " + selectedVariant!!.id
            )
        } else {
            selectedVariant = modelClass.variantsList!![0].copy()
            Log.d("selecteddcolorinmod", selectedVariant!!.color.toString())
        }
        var view: View = inflater.inflate(R.layout.fragment_product_layout_2, container, false)
        initializeViews(view)
        (activity as MainActivity).lockDrawer()
        toolbar = view.findViewById(R.id.productToolbar)
        setHasOptionsMenu(true)
//        for (selectedVariant in variantList!!)
//            Log.d(
//                "selecteddcolorr",
//                "${selectedVariant!!.color} ${selectedVariant!!.size} ${selectedVariant!!.id}"
//            )
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
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    htmlDescriptionScroll!!.viewTreeObserver.addOnScrollChangedListener {
                        bottomSheetBehavior!!.isDraggable = htmlDescriptionScroll!!.scrollY == 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0.9f) {
                    toolbar!!.visibility = View.INVISIBLE
                } else
                    toolbar!!.visibility = View.VISIBLE
            }
        })




        colorRecyclerAdapter = ProductColorRecyclerViewAdapter(this) { colorr ->

            val isVariantAvailable =
                variantList!!.find { it.color == colorr && it.size == selectedVariant!!.size }

            if (isVariantAvailable != null) {
                val isVariantavailableInApplicationClass =
                    ApplicationClass.selectedVariantList!!.find { it.id == isVariantAvailable.id }

                if (isVariantavailableInApplicationClass == null) {
                    selectedVariant = isVariantAvailable.copy(quantityOfItem = 0)
                    addTocartButton!!.visibility = View.VISIBLE
                    addOrRemoveItemsLinear!!.visibility = View.GONE
                } else {
                    selectedVariant = isVariantavailableInApplicationClass.copy()
                    addTocartButton!!.visibility = View.GONE
                    addOrRemoveItemsLinear!!.visibility = View.VISIBLE
                }
            } else
                Toast.makeText(
                    context,
                    "selected color or size is not available",
                    Toast.LENGTH_SHORT
                ).show()

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

                val isVariantAvailable =
                    variantList!!.find { it.color == selectedVariant!!.color && it.size == sizee }


                if (isVariantAvailable != null) {

                    val isVariantavailableInApplicationClass =
                        ApplicationClass.selectedVariantList!!.find { it.id == isVariantAvailable.id }

                    if (isVariantavailableInApplicationClass == null) {

                        selectedVariant = isVariantAvailable.copy(quantityOfItem = 0)

                        addTocartButton!!.visibility = View.VISIBLE
                        addOrRemoveItemsLinear!!.visibility = View.GONE
                    } else {
                        selectedVariant = isVariantavailableInApplicationClass.copy()
                        addTocartButton!!.visibility = View.GONE
                        addOrRemoveItemsLinear!!.visibility = View.VISIBLE
                    }
                } else
                    Toast.makeText(
                        context,
                        "selected color or size is not available",
                        Toast.LENGTH_SHORT
                    ).show()


                itemPriceTextView!!.text =
                    "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant!!.price}"

                itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

                Log.d("selecteditems", ApplicationClass.selectedVariantList.toString())

                Log.d(
                    "selecteddsize",
                    "${selectedVariant!!.color} ${selectedVariant!!.size} ${selectedVariant!!.id}"
                )
            }


        sizeRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        sizeRecyclerView!!.adapter = sizeRecyclerViewAdapter

        val a: MutableList<VariantsModelClass> = ArrayList()
        for (i in variantList!!) {
            Log.d("hello", i.color + "  " + (i.id == selectedVariant!!.id))
            a.add(i.copy())
        }

        colorRecyclerAdapter!!.submitList(a.filter {
            it.isSelected = it.color == selectedVariant!!.color
            Log.d("itselected", it.color + " " + it.isSelected + "  " + it.id)
            return@filter true
        }.distinctBy { it.color } as MutableList<VariantsModelClass>
        )

        sizeRecyclerViewAdapter!!.submitList(
            a
                .filter {
                    it.isSelected = it.size == selectedVariant!!.size
                    return@filter true
                }
                .sortedBy { it.size }.distinctBy { it.size }.toMutableList()
        )



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
            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
        }

        itemAddImageView!!.setOnClickListener {
            selectedVariant!!.quantityOfItem++
            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
            selectedVariant!!.isSelected = true

            ApplicationClass.selectedVariantList?.find {
                it.id == selectedVariant!!.id
                        && it.parentProductId == selectedVariant!!.parentProductId
            }!!.quantityOfItem =
                selectedVariant!!.quantityOfItem

            Log.d("selecteditems", ApplicationClass.selectedVariantList.toString())
            showOrHideItemCountIndicator()
        }




        addTocartButton!!.setOnClickListener {
            //adding product model to selected items
            if (ApplicationClass.selectedVariantList!!.find { it.id == selectedVariant!!.id } == null) {
                selectedVariant!!.quantityOfItem++
                selectedVariant!!.isSelected = true
                ApplicationClass.selectedVariantList!!.add(selectedVariant!!.copy())
                addTocartButton!!.visibility = View.GONE
                addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            }
            Log.d("selecteditems", ApplicationClass.selectedVariantList.toString())
            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
            showOrHideItemCountIndicator()
        }




        itemremoveImageView!!.setOnClickListener {
            if (selectedVariant!!.quantityOfItem > 0) {
                selectedVariant!!.quantityOfItem--
                ApplicationClass.selectedVariantList?.find {
                    it.id == selectedVariant!!.id

                }!!.quantityOfItem =
                    selectedVariant!!.quantityOfItem
                Log.d("selecteditems", ApplicationClass.selectedVariantList.toString())
            }

            if (selectedVariant!!.quantityOfItem == 0 && ApplicationClass.selectedVariantList?.find { it.id == selectedVariant!!.id } != null) {
                addTocartButton!!.visibility = View.VISIBLE
                addOrRemoveItemsLinear!!.visibility = View.GONE

                selectedVariant!!.isSelected = false

                (ApplicationClass.selectedVariantList)!!.remove(selectedVariant!!)
            }
            showOrHideItemCountIndicator()

        }
        itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

        itemNameTextView!!.text = modelClass.itemName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemTotalDescriptionTextView!!.getSettings().setBuiltInZoomControls(true);

            itemTotalDescriptionTextView!!.getSettings().setJavaScriptEnabled(true);
            itemTotalDescriptionTextView!!.getSettings().setLoadWithOverviewMode(true);

            itemTotalDescriptionTextView!!.loadDataWithBaseURL(null,modelClass.itemDescriptionText!!,"text/html", "utf-8",null)


        } else {
           //                                                                                                                                                                                                  itemTotalDescriptionTextView!!.text = (Html.fromHtml(modelClass.itemDescriptionText));
        }
//        itemTotalDescriptionTextView!!.text = modelClass.itemDescriptionText


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

        htmlDescriptionScroll = view.findViewById(R.id.htmldescriptionscrollview)
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