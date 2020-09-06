package com.example.myfirstofficeappecommerce.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.MainRecyclerAdapter
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.R


class SearchFragment : Fragment() {

    var actionbar: Toolbar? = null
    var searchEditText: EditText? = null
    var recyclr: RecyclerView? = null
    var mainRecyclerAdapter: MainRecyclerAdapter? = null
    var imgr: InputMethodManager? = null
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
        mainRecyclerAdapter = MainRecyclerAdapter(
            (activity as MainActivity),
            CategoriesDataProvider().getMapDataForCategories(),
            Constants.SCROLL_TYPE
        )

        recyclr!!.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        recyclr!!.adapter = mainRecyclerAdapter
        searchEditText?.requestFocus()
        imgr =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    recyclr!!.adapter = null
                    mainRecyclerAdapter = MainRecyclerAdapter(
                        (activity as MainActivity),
                        CategoriesDataProvider().getMapDataForCategories(),
                        Constants.SEARCH_TYPE
                    )

                    recyclr!!.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    recyclr!!.adapter = mainRecyclerAdapter
                    mainRecyclerAdapter!!.submitList(
                        CategoriesDataProvider().getSearhItemsData()
                    )
                }
            }
        })

        mainRecyclerAdapter!!.submitList(
            CategoriesDataProvider().getMapDataForCategories()?.get("Phones")
        )
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("menuclicked", "yes")
        if (item.itemId == android.R.id.home) {
            imgr?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}