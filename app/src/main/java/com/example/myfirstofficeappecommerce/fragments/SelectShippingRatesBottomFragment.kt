package com.example.myfirstofficeappecommerce.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.ShippingRatesAdapter
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.ChangeShippingRatesLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectShippingRatesBottomFragment(
    var checkoutId: String,
    var shippingList: MutableList<UserDetailsModelClass>,
    var callback: (Int) -> Unit
) : BottomSheetDialogFragment() {
    var shippingRatesAdapter: ShippingRatesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(context!!)
            .inflate(R.layout.change_shipping_rates_layout, container, false)
        var binding1 = ChangeShippingRatesLayoutBinding.bind(view)

        shippingRatesAdapter = ShippingRatesAdapter(this, checkoutId) { clickedPos ->

            callback(clickedPos)

        }
        binding1!!.changeshippingaddressrecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding1!!.changeshippingaddressrecycler.adapter = shippingRatesAdapter
        shippingRatesAdapter!!.submitList(shippingList)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =

                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).skipCollapsed = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).isHideable = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).peekHeight = 0
        }
        return bottomSheetDialog
    }
}