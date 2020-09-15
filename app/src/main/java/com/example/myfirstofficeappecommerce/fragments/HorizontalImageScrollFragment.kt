package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R


class HorizontalImageScrollFragment(
    var mainFragment: Fragment,
    var listDataForHorizontalScroll:ModelClass

) : Fragment() {
    var imageView: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragment", (mainFragment is ProductFragment).toString())
        var view: View? = null
        if (mainFragment is MainFragment) {
            view = inflater.inflate(R.layout.fragment_image_scroll, container, false)
        } else {
            view = inflater.inflate(R.layout.product_fragment_imagescroll_layout, container, false)
            imageView = view.findViewById(R.id.productpageImageView)
            Glide.with(requireActivity()).load(listDataForHorizontalScroll.imageUrl).into(imageView!!)
        }
        return view
    }


}