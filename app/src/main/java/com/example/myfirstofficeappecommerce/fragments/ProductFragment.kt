package com.example.myfirstofficeappecommerce.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.Menu
import android.view.animation.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Adapters.HorizontalScrollViewPagerAdapter
import com.example.myfirstofficeappecommerce.Adapters.ProductColorRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Adapters.ProductSizeRecyclerViewAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModel
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModelFactory
import com.example.myfirstofficeappecommerce.databinding.FragmentProductLayout2Binding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_product_layout_2.view.*


class ProductFragment(private var modelClass: CategoriesModelClass) : Fragment() {


    private var viewPager2: ViewPager2? = null
    private var tablayout: TabLayout? = null;
    var toolbar: Toolbar? = null
    private var itemNameTextView: TextView? = null
    private var itemPriceTextView: TextView? = null
    private var webview: WebView? = null
    private var itemQuantitiyTextView: TextView? = null
    private var itemMessagesImageView: ImageView? = null
    private var itemShareImageView: ImageView? = null
    private var itemAddImageView: ImageView? = null
    private var itemremoveImageView: ImageView? = null
    var menu: Menu? = null
    private var addTocartButton: Button? = null
    private var addOrRemoveItemsLinear: LinearLayout? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private var colorRecyclerView: RecyclerView? = null
    private var sizeRecyclerView: RecyclerView? = null
    private var colorRecyclerAdapter: ProductColorRecyclerViewAdapter? = null
    private var sizeRecyclerViewAdapter: ProductSizeRecyclerViewAdapter? = null
    private var selectedVariant: VariantsModelClass? = null
    private var variantList: List<VariantsModelClass>? = null
    private var htmlDescriptionScroll: ScrollView? = null
    lateinit var progressbar:ProgressBar
    var binding: FragmentProductLayout2Binding? = null
    var type:String="1"
    var bottomsheetProgressbar:ProgressBar?=null
    var rootlayout:CoordinatorLayout?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        if (ApplicationClass.recentsList!!.find { it.parentProductId == modelClass.id } == null) {
            var recentItem = modelClass.variantsList!![0]
            recentItem.isRecent = true
            ApplicationClass.recentsList!!.add(recentItem)
        }

        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View?=null
        if(type=="2")
        view= inflater.inflate(R.layout.fragment_product_layout_2, container, false)
        else view=inflater.inflate(R.layout.fragment_product, container, false)

        initializeViews(view)
        if(type=="2")
        binding = FragmentProductLayout2Binding.bind(view)

        binding?.productfragmentouterlayout?.visibility = View.GONE

        (activity as MainActivity).lockDrawer()

        getProductData()

        setHasOptionsMenu(true)

        setUpToolbar(view)

        setUpProductImageViewPager(view)

        if(type=="2")
            setUpBottomSheet(view)

        setUpProductColorRecyclerView()

        setupProductSizeRecyclerView()

        setUpClickListeners()

        binding?.textView2?.text = modelClass.variantparam0

        binding?.textView4?.text = modelClass.variantParam1

        return view
    }

    private fun getProductData() {
        var viewmodel = ViewModelProvider(this, CategoriesViewModelFactory(modelClass.id)).get(
            CategoriesViewModel::class.java
        )
        viewmodel.getVariantData()
            .observe(this, Observer {
                if (it.size > 0) {
                    variantList = it.toList()

                    getSelectedVariant()
                    assignDataToViews()
                    submitDataToSizeColorAdapters()
                    binding?.productfragmentprogressbar?.visibility = View.GONE
                    progressbar.visibility=View.GONE
                    binding?.productfragmentouterlayout?.visibility = View.VISIBLE
                }
            })
    }

    private fun getSelectedVariant() {
        if (ApplicationClass.selectedVariantList?.find { it.parentProductId == modelClass.id && it.isSelected } != null) {
            selectedVariant =
                ApplicationClass.selectedVariantList?.find { it.parentProductId == modelClass.id && it.isSelected }
                    ?.copy()

        } else {
            selectedVariant = variantList!![0].copy()

        }
    }


    private fun submitDataToSizeColorAdapters() {

        val a: MutableList<VariantsModelClass> = ArrayList()
        for (i in variantList!!) {

            a.add(i.copy())
        }

        colorRecyclerAdapter!!.submitList(a.filter {
            it.isSelected = it.color == selectedVariant!!.color

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
    }


    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.productToolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    private fun setUpProductImageViewPager(view: View) {
        viewPager2 = view.findViewById(R.id.productviewpager)
        (viewPager2 as ViewPager2).adapter =
            HorizontalScrollViewPagerAdapter(
                this,
                modelClass.imageSrcOfVariants,
                Constants.HORIZONTAL_SCROLL_PRODUCT_FRAG
            )

        TabLayoutMediator(
            (tablayout as TabLayout),
            (viewPager2 as ViewPager2)
        ) { tab: TabLayout.Tab, i: Int ->

        }.attach()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun assignDataToViews() {

        if (selectedVariant!!.quantityOfItem > 0) {
            addTocartButton!!.visibility = View.GONE
            addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
        }


        itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()

        itemNameTextView!!.text = modelClass.itemName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webview!!.settings.builtInZoomControls = true;

            webview!!.settings.javaScriptEnabled = true;
            webview!!.settings.loadWithOverviewMode = true;
            webview!!.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding?.bottomsheeet?.productbottomsheetprogressbar?.visibility = View.GONE
                    bottomsheetProgressbar?.visibility=View.GONE
                    Log.d("camehere","ok")
                    super.onPageFinished(view, url)
                }
            }
            webview!!.loadDataWithBaseURL(
                null,
                modelClass.itemDescriptionText!!,
                "text/html",
                "utf-8",
                null
            )

            htmlDescriptionScroll?.viewTreeObserver?.addOnScrollChangedListener {
                bottomSheetBehavior?.isDraggable = !htmlDescriptionScroll!!.canScrollVertically(-1)


            }

            binding?.bottomsheeet?.bottomsheet?.setOnTouchListener { v, event ->

                bottomSheetBehavior?.state=BottomSheetBehavior.STATE_COLLAPSED
                htmlDescriptionScroll?.scrollTo(0,0)
               return@setOnTouchListener true
            }

        } else {
            Toast.makeText(
                context,
                "Sorry WebView is not supported in your device",
                Toast.LENGTH_SHORT
            ).show()
        }

        itemPriceTextView!!.text =
            "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant!!.price}"
    }


    private fun setUpClickListeners() {
        itemShareImageView!!.setOnClickListener {
            var s = modelClass.itemName
            var intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html";
            intent.putExtra(Intent.EXTRA_TEXT, s)
            activity!!.startActivity(intent)
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


            showOrHideItemCountIndicator()
        }




        addTocartButton!!.setOnClickListener {


            //adding product model to selected items
            if (ApplicationClass.selectedVariantList?.find { it.id == selectedVariant?.id } == null && selectedVariant != null && selectedVariant!!.quantityOfItem >= 0) {
                selectedVariant!!.quantityOfItem++
                selectedVariant!!.isSelected = true

                ApplicationClass.selectedVariantList!!.add(selectedVariant!!.copy())
                addTocartButton!!.visibility = View.INVISIBLE
                addOrRemoveItemsLinear!!.visibility = View.VISIBLE
            } else
                Toast.makeText(context, "Please choose a color or size", Toast.LENGTH_SHORT).show()

            itemQuantitiyTextView!!.text = selectedVariant?.quantityOfItem.toString()
            if (selectedVariant != null && selectedVariant!!.quantityOfItem > 0)
                startCartAnimation()

        }




        itemremoveImageView!!.setOnClickListener {
            if (selectedVariant!!.quantityOfItem > 0) {
                selectedVariant!!.quantityOfItem--
                ApplicationClass.selectedVariantList?.find {
                    it.id == selectedVariant!!.id

                }!!.quantityOfItem =
                    selectedVariant!!.quantityOfItem

                itemQuantitiyTextView!!.text = selectedVariant!!.quantityOfItem.toString()
            }

            if (selectedVariant!!.quantityOfItem == 0 && ApplicationClass.selectedVariantList?.find { it.id == selectedVariant!!.id } != null) {
                addTocartButton!!.visibility = View.VISIBLE
                addOrRemoveItemsLinear!!.visibility = View.GONE

                (ApplicationClass.selectedVariantList)!!.remove(selectedVariant!!).toString()


            }

            showOrHideItemCountIndicator()

        }
    }


    private fun startCartAnimation() {
        var cartPosition: IntArray = IntArray(2)
        addTocartButton!!.getLocationOnScreen(cartPosition)

        var a = menu!!.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem)


        var textView: TextView = TextView(context)
        textView.setTextColor(Color.RED)

        textView.text = selectedVariant!!.quantityOfItem.toString()

        textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)

        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if(type=="2")
        binding?.root?.coordinator?.addView(textView)
        else
            rootlayout?.addView(textView)

        var addCartButtonPosition: IntArray = IntArray(2)
        var cartPositionn: IntArray = IntArray(2)
        addTocartButton!!.getLocationOnScreen(addCartButtonPosition)
        menu!!.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem)

            .getLocationOnScreen(cartPositionn)


//animate Y property of textview

        ObjectAnimator.ofFloat(
            textView,
            "translationY", cartPosition[1].toFloat(), cartPositionn[1].toFloat() - 100
        )
            .apply {

                interpolator = LinearInterpolator()
                duration = 1000

            }.start()

//animate x property of textview

        ObjectAnimator.ofFloat(
            textView,
            "translationX", addCartButtonPosition[0].toFloat(), cartPositionn[0].toFloat()
        )
            .apply {

                interpolator = LinearInterpolator()
                duration = 1000

                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {


                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        val scale = ScaleAnimation(
                            0f,
                            1f,
                            0f,
                            1f,
                            ScaleAnimation.RELATIVE_TO_PARENT,
                            .5f,
                            ScaleAnimation.RELATIVE_TO_PARENT,
                            .5f
                        )
                        scale.duration = 500
                        scale.interpolator = BounceInterpolator()
                        a.startAnimation(scale)

                        ObjectAnimator.ofFloat(
                            a,
                            "rotationY", 0f, 360f
                        )
                            .apply {

                                interpolator = BounceInterpolator()
                                duration = 100
                                repeatCount = 3

                            }.start()
                        textView.visibility = View.GONE

                        showOrHideItemCountIndicator()
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationRepeat(p0: Animator?) {

                    }
                })
            }.start()
    }

    private fun setupProductSizeRecyclerView() {
        sizeRecyclerViewAdapter =
            ProductSizeRecyclerViewAdapter { sizee ->

                val isVariantAvailable =
                    variantList!!.find { it.color == selectedVariant!!.color && it.size == sizee }
                Log.d("variantlistselcetcolo", " " + selectedVariant!!.color + "  " + sizee)
//                var a = variantList!!.filter {
//                    it.isVariantAvailable = sizee == it.size
//                    return@filter true
//                }
//                colorRecyclerAdapter!!.submitList(a.distinctBy { it.color } as MutableList<VariantsModelClass>)

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


            }


        sizeRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        sizeRecyclerView!!.adapter = sizeRecyclerViewAdapter
    }


    private fun setUpProductColorRecyclerView() {
        colorRecyclerAdapter = ProductColorRecyclerViewAdapter(this) { colorr ->
            val isVariantAvailable =
                variantList!!.find { it.color == colorr && it.size == selectedVariant!!.size }

            var sizes = ArrayList<String>()
            for (i in sizeRecyclerViewAdapter!!.currentList) {
                sizes.add(i.size!!)
            }


            var a = variantList!!.filter {
                Log.d("test", sizes.toString() + "  " + it.size)
                Log.d("test", " " + colorr + "   " + it.color)
                var a = it.copy()
                a.isVariantAvailable = colorr == a.color

                return@filter colorr == a.color

            }

            sizeRecyclerViewAdapter!!.submitList(a.filter { it.isVariantAvailable }
                .distinctBy { it.size }
                .sortedBy { it.size } as MutableList<VariantsModelClass>)


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
            } else {
                Toast.makeText(
                    context,
                    "selected color or size is not available",
                    Toast.LENGTH_SHORT
                ).show()
                selectedVariant!!.price = 0f
                selectedVariant!!.quantityOfItem = -1
                selectedVariant!!.id = ""
                selectedVariant!!.parentProductId = ""
                selectedVariant!!.color = colorr
                selectedVariant!!.size = ""
            }

            itemQuantitiyTextView!!.text = selectedVariant?.quantityOfItem.toString()

            itemPriceTextView!!.text =
                "MRP : ${activity!!.getString(R.string.Rs)} ${selectedVariant?.price}"


        }

        colorRecyclerView!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        colorRecyclerView!!.adapter = colorRecyclerAdapter
    }


    private fun setUpBottomSheet(view: View) {
        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bottomsheeet.bottomsheet)


        binding?.bottomsheeet?.bottomsheet?.layoutParams?.height =
            resources.displayMetrics.heightPixels - (56 * resources.displayMetrics.density).toInt()

        bottomSheetBehavior!!.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //      bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED


                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                if (slideOffset >= 0.7f) {


                }
            }
        })
    }


    private fun initializeViews(view: View) {

        colorRecyclerView = view.findViewById(R.id.productcolorRecyclerView)

        sizeRecyclerView = view.findViewById(R.id.productsizeRecyclerview)

        tablayout = view.findViewById(R.id.producttablayout)

        itemNameTextView = view.findViewById(R.id.productItemNameTextView)

        itemPriceTextView = view.findViewById(R.id.productPagePriceTextView)

        itemQuantitiyTextView = view.findViewById(R.id.productitemquantitiytextview)

        webview = view.findViewById(R.id.productpageDescriptionTextView)

        itemMessagesImageView = view.findViewById(R.id.productPageMessagesImageView)

        itemShareImageView = view.findViewById(R.id.productPageShareTImageView)

        itemAddImageView = view.findViewById(R.id.productadditemsImageButton)

        itemremoveImageView = view.findViewById(R.id.productremoveitemsImageButton)

        addTocartButton = view.findViewById(R.id.productaddToCartButton)

        addOrRemoveItemsLinear = view.findViewById(R.id.productaddorremoveitemslinearlayout)

        htmlDescriptionScroll = view.findViewById(R.id.htmldescriptionscrollview)

        progressbar=view.findViewById(R.id.productfragmentprogressbar)

        bottomsheetProgressbar=view.findViewById(R.id.productbottomsheetprogressbar)

        rootlayout=view.findViewById(R.id.coordinator)
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