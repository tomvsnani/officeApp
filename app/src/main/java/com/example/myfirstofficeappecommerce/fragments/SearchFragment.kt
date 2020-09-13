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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Adapters.MainRecyclerAdapter
import com.example.myfirstofficeappecommerce.Adapters.searchfragmentRecyclerAdapter
import com.example.myfirstofficeappecommerce.Models.CategoriesModelClass


open class SearchFragment() : Fragment() {

    var actionbar: Toolbar? = null
    var searchEditText: EditText? = null
    var recyclr: RecyclerView? = null
    var searchfragmentRecyclerAdapter: searchfragmentRecyclerAdapter? = null

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

        CategoriesDataProvider.mutablehashmap.observeForever {
            searchfragmentRecyclerAdapter = searchfragmentRecyclerAdapter(
                (this),
               it,
                Constants.SCROLL_TYPE
            )

            recyclr!!.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
            recyclr!!.adapter = searchfragmentRecyclerAdapter
            recyclr!!.itemAnimator = null
        }




        searchEditText?.requestFocus()
        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    recyclr!!.adapter = null
                    searchfragmentRecyclerAdapter = searchfragmentRecyclerAdapter(
                        (this@SearchFragment),
                        CategoriesDataProvider.getMapDataForCategories(),
                        Constants.SEARCH_TYPE
                    )

                    recyclr!!.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    recyclr!!.adapter = searchfragmentRecyclerAdapter
                    searchfragmentRecyclerAdapter!!.submitList(
                        CategoriesDataProvider.getSearhItemsData().filter {
                            it.itemName.contains(
                                p0.toString(), true
                            )
                        } as MutableList<CategoriesModelClass>
                    )
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
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        menu.findItem(R.id.cartmenu).actionView.findViewById<ImageView>(R.id.cartmenuitem).setOnClickListener {
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0 until menu.size()) {
            if(menu.getItem(i).itemId!=R.id.cartmenu)
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
            menu!!.findItem(R.id.cartmenu).actionView.findViewById<TextView>(R.id.cartitemNumberIndicatormenu).visibility =
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
        exitTransition= inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }

}