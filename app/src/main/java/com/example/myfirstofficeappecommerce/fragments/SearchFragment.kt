package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myfirstofficeappecommerce.R


class SearchFragment : Fragment() {

    var actionbar: Toolbar? = null
    var searchEditText: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false);
        actionbar = view.findViewById(R.id.searchFragmenttoolbar)
        (activity as AppCompatActivity).setSupportActionBar(actionbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        searchEditText= actionbar!!.getChildAt(0) as EditText?
        searchEditText?.requestFocus()

        return view
    }


}