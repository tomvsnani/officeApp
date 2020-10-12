package com.example.myfirstofficeappecommerce.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.searchfragmentRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModel
import com.example.myfirstofficeappecommerce.Viewmodel.CategoriesViewModelFactory
import com.shopify.buy3.*


open class SearchFragment() : Fragment() {

    var actionbar: Toolbar? = null
    var searchEditText: EditText? = null
    var recyclr: RecyclerView? = null
    var searchfragmentRecyclerAdapter: searchfragmentRecyclerAdapter? = null
    var searchfragmentRecyclerAdapterSearch: searchfragmentRecyclerAdapter? = null

    var menu: Menu? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true)
        actionbar = view.findViewById(R.id.searchFragmenttoolbar)
        (activity as AppCompatActivity).setSupportActionBar(actionbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        searchEditText = actionbar!!.getChildAt(0) as EditText?
        recyclr = view.findViewById(R.id.searchviewrecycler)
        (activity as MainActivity).lockDrawer()
        searchfragmentRecyclerAdapterSearch = searchfragmentRecyclerAdapter(
            (this),
            Constants.SEARCH_FRAG_SEARCH_TYPE
        )

        searchfragmentRecyclerAdapter = searchfragmentRecyclerAdapter(
            (this),

            Constants.SEARCH_FRAG_SCROLL_TYPE
        )

        CategoriesDataProvider.mutableCollectionList.observeForever {

            recyclr!!.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
            recyclr!!.adapter = searchfragmentRecyclerAdapter
            recyclr!!.itemAnimator = null
            searchfragmentRecyclerAdapter!!.submitList(it)
        }

        searchEditText?.requestFocus()
        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    // recyclr!!.adapter = null


                    recyclr!!.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    recyclr!!.adapter = searchfragmentRecyclerAdapterSearch


                    val call: QueryGraphCall =
                        CategoriesDataProvider.graphh!!.queryGraph(getQuery(p0.toString()))
                    call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
                        var collectionList: MutableList<CategoriesModelClass> = ArrayList()
                        override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {

                            for (productEdge in response.data()?.shop?.products?.edges!!) {

                                var productImageSrcList: MutableList<ModelClass> = ArrayList()

                                // adding aa images to product imagelist
                                for (imageedge in productEdge.node.images.edges)
                                    productImageSrcList.add(ModelClass(imageUrl = imageedge.node.src))

                                var variantList: MutableList<VariantsModelClass> = ArrayList()

                                //adding all variant data to product variant list
                                for (variantEdge in productEdge.node.variants.edges) {
                                    var sizeIndex = 2;
                                    if (variantEdge.node.selectedOptions.size > 1 && variantEdge.node.selectedOptions[1].name == "Size")
                                        sizeIndex = 1
                                    variantList.add(
                                        VariantsModelClass(
                                            variantEdge.node.id.toString(),
                                            productEdge.node.id.toString(),
                                            if (variantEdge.node.selectedOptions.size > 0) variantEdge.node.selectedOptions[0].value else null,
                                            if (variantEdge.node.selectedOptions.size > 1 && sizeIndex < variantEdge.node.selectedOptions.size) variantEdge.node.selectedOptions[sizeIndex].value else null,
                                            variantEdge.node.price.toFloat(),
                                            name = productEdge.node.title


                                        )
                                    )
                                }

                                var productmodelclass = CategoriesModelClass(
                                    id = productEdge.node.id.toString(),
                                    itemName = productEdge.node.title,
                                    itemDescriptionText = productEdge.node.descriptionHtml,
                                    imageSrcOfVariants = productImageSrcList,
                                    realTimeMrp = productEdge.node.variants.edges[0].node.price.precision(),
                                    variantsList = variantList,
                                    cursor = productEdge.cursor ?: null
                                )


                                collectionList.add(productmodelclass)
                            }

                            recyclr!!.post {
                                searchfragmentRecyclerAdapterSearch!!.submitList(
                                    collectionList
                                )
                            }

                        }

                        override fun onFailure(error: GraphError) {
                            Log.d("connectionfail", error.message.toString())
                        }
                    })

                }
            }
        })

        return view
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("menuclicked", "yes")
        if (item.itemId == android.R.id.home) {
            showSoftwareKeyboard(true)

        }
        if( item.itemId == R.id.profilemenu ) {
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container,
                ProfileFragment(Constants.NORMAL_SIGN_IN)
            ).addToBackStack(null)
                .commit()

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        menu.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem)
            .setOnClickListener {
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


    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).itemId != R.id.cartmenu)
                menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }


    fun showOrHideItemCountIndicator() {
        var itemCount = Utils.getItemCount()

        if (itemCount.toInt() > 0) {

            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)
                .apply {
                    text = itemCount.toString()
                    visibility = View.VISIBLE
                }

        } else {
            menu?.findItem(R.id.cartmenu)?.actionView?.findViewById<TextView>(R.id.cartitemNumberIndicatormenu)?.visibility =
                View.INVISIBLE


        }
    }


    protected fun showSoftwareKeyboard(showKeyboard: Boolean) {

        val inputManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        searchEditText!!.clearFocus()
        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//        Thread.sleep(2000)
        activity?.onBackPressed()


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }


    fun getQuery(p0: String): Storefront.QueryRootQuery {
        return Storefront.query { rootQuery: Storefront.QueryRootQuery ->
            rootQuery.shop { shopQuery: Storefront.ShopQuery ->
                shopQuery.products({ args: Storefront.ShopQuery.ProductsArguments? ->
                    args!!.query("title:*${p0.toString()}*").first(10)
                }, { _queryBuilder ->
                    _queryBuilder.edges { _queryBuilder ->
                        _queryBuilder.cursor()
                        _queryBuilder.node { _queryBuilder ->
                            _queryBuilder.title()

                                .descriptionHtml()

                                .images({ args: Storefront.ProductQuery.ImagesArguments? ->
                                    args!!.first(
                                        1
                                    )
                                }, {
                                    it.edges { _queryBuilder ->
                                        _queryBuilder.node { _queryBuilder ->
                                            _queryBuilder.src().id()
                                        }
                                    }
                                })


                                .variants({ args: Storefront.ProductQuery.VariantsArguments? ->
                                    args!!.first(
                                        1
                                    )
                                },
                                    { _queryBuilder ->
                                        _queryBuilder.edges { _queryBuilder ->
                                            _queryBuilder.node { _queryBuilder ->
                                                _queryBuilder.price()
                                                    .selectedOptions { _queryBuilder ->
                                                        _queryBuilder.name().value()
                                                    }


                                            }
                                        }
                                    })


                        }
                    }
                })
            }
        }
    }



}