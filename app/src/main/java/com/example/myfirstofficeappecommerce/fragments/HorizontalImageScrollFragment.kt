package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R


class HorizontalImageScrollFragment(
    var mainFragment: Fragment,
    var listDataForHorizontalScroll: List<ModelClass>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragment",(mainFragment is ProductFragment).toString())
        return if (mainFragment is MainFragment)
            inflater.inflate(R.layout.fragment_image_scroll, container, false)
        else inflater.inflate(R.layout.product_fragment_imagescroll_layout, container, false)
    }


}