package com.example.myfirstofficeappecommerce.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentShowAddressBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopify.buy3.Storefront
import java.lang.Exception


class BottomSheetFragment(
    var totalTax: Float,
    var checkoutId: String,
    var finalisingFragment: FinalisingOrderFragment
) : BottomSheetDialogFragment() {
    var adapter: ChooseAddressRecyclerAdapter? = null
    var binding: FragmentShowAddressBottomsheetBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_show_address_bottomsheet, container, true)
        binding = FragmentShowAddressBottomsheetBinding.bind(view)
        var recyclerView = binding!!.chooseaddressrecyclerview
        adapter = ChooseAddressRecyclerAdapter(this, checkoutId)
        recyclerView = view.findViewById(R.id.chooseaddressrecyclerview)
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = adapter
        var addressList = ApplicationClass.addressList


        if (addressList.size <= 0)
            binding!!.chooseaddressrecyclerview.visibility = View.GONE
        else
            binding!!.chooseaddressrecyclerview.visibility = View.VISIBLE

        binding!!.confirmButton.setOnClickListener {
            finalisingFragment.doTasksBasedOnSelectedAddress(Storefront.MailingAddress().apply {
                city = getDeliveryAddress(addressList)?.city
                province = getDeliveryAddress(addressList)?.state
                country = getDeliveryAddress(addressList)?.country
                name = getDeliveryAddress(addressList)?.title
                zip = getDeliveryAddress(addressList)?.pinCode
                phone = getDeliveryAddress(addressList)?.phoneNumber


            })
            isCancelable=true
            dismiss()
        }


        activity!!.runOnUiThread {

            var a =
                getDeliveryAddress(addressList)

            if (a != null) {

                a!!.isSelectedAddress = true

                addressList.remove(a)

                addressList.add(0, a)

                adapter!!.submitList(addressList)

                adapter!!.notifyDataSetChanged()

            } else Toast.makeText(context!!, "Add an address", Toast.LENGTH_SHORT).show()


        }

        binding!!.viewmoreaddressesbutton.setOnClickListener {

            NewAddressFragment(
                checkoutId,
                totalTax,this
            ).show(parentFragment!!.childFragmentManager, "")


        }
        return view
    }

    private fun getDeliveryAddress(addressList: MutableList<UserDetailsModelClass>): UserDetailsModelClass? {
        return (addressList.find { it.isSelectedAddress }

            ?: addressList.find { it.id == ApplicationClass.defaultAdress?.id.toString() })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        isCancelable=false


        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =

                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            var behaviour=  BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).skipCollapsed = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).isHideable = true
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).peekHeight = 0
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                 try {
                     behaviour.state=BottomSheetBehavior.STATE_EXPANDED
                 }catch (e:Exception){}

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }
        return bottomSheetDialog
    }


}