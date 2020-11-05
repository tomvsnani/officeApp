package com.example.myfirstofficeappecommerce.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ProductFragmentImagescrollLayoutBinding


class HorizontalImageScrollFragment(
    var mainFragment: Fragment,
    var listDataForHorizontalScroll: UserDetailsModelClass,
    var type: String

) : Fragment() {
    var imageView: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragment", (mainFragment is ProductFragment).toString())
        var view: View? = null
        //var horizontalScrollBinding=HorizontalScrollBanner2Binding.bind()
        if (type == Constants.HORIZONTAL_SCROLL_TYPE_BANNER1 || type == Constants.HORIZONTAL_SCROLL_TYPE_BANNER2) {
            view = inflater.inflate(R.layout.horizontal_scroll_banner1, container, true)

            imageView = view.findViewById(R.id.scrollableimageviewviewpagerbanner1)
            imageView!!.layoutParams.height =
                if (type == Constants.HORIZONTAL_SCROLL_TYPE_BANNER1) 200 * resources.displayMetrics.density.toInt()
                else 500 * resources.displayMetrics.density.toInt()

        } else if (type == Constants.HORIZONTAL_SCROLL_PRODUCT_FRAG) {

            view = inflater.inflate(R.layout.product_fragment_imagescroll_layout, container, true)

            Glide.with(requireActivity()).load(listDataForHorizontalScroll.imageUrl)
                .into(
                    ProductFragmentImagescrollLayoutBinding
                        .bind(view)
                        .productpageImageView
                )
        }
        return view
    }
}