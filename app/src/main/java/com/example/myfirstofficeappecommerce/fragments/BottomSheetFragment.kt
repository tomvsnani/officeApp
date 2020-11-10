package com.example.myfirstofficeappecommerce.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.Adapters.ChooseAddressRecyclerAdapter
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.RunGraphQLQuery
import com.example.myfirstofficeappecommerce.databinding.FragmentShowAddressBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopify.buy3.Storefront


class BottomSheetFragment(

    var checkoutId: String = "",
    var finalisingFragment: Fragment
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
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = adapter
        var addressList = ApplicationClass.addressList

        binding!!.closeBottomSheetImageView.setOnClickListener { dismiss() }


        if (addressList.size <= 0)
            binding!!.chooseaddressrecyclerview.visibility = View.GONE
        else
            binding!!.chooseaddressrecyclerview.visibility = View.VISIBLE

        if (ApplicationClass.addresstype == Constants.ADD_ADDRESS_TYPE_USER_ADDRESS) {
            binding!!.confirmButton.visibility = View.GONE
            RunGraphQLQuery.retrieve_all_the_addresses((activity as MainActivity)).observe(this) {

                ApplicationClass.addressList = it as MutableList<UserDetailsModelClass>
                adapter?.submitList(it)
            }
        } else
            binding!!.confirmButton.visibility = View.VISIBLE

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

        initializeClickListeners(addressList)

        return view
    }

    private fun initializeClickListeners(addressList: MutableList<UserDetailsModelClass>) {
        binding!!.confirmButton.setOnClickListener {
            doTasksBasedOnSelectedAddress(addressList)
            isCancelable = true
            dismiss()
        }


        binding!!.viewmoreaddressesbutton.setOnClickListener {
            if (ApplicationClass.addresstype == Constants.ADD_ADDRESS_TYPE_ORDER_ADDRESS)
                NewAddressFragment(
                    checkoutId,
                    this
                ).show(parentFragment?.childFragmentManager!!, "")
            else
                NewAddressFragment(
                    checkoutId,
                    this
                ).show(activity!!.supportFragmentManager, "")

        }
    }

    fun doTasksBasedOnSelectedAddress(addressList: MutableList<UserDetailsModelClass>) {
        if (addressList.size > 0) {
            (finalisingFragment as FinalisingOrderFragment).doTasksBasedOnSelectedAddress(
                Storefront.MailingAddress().apply {
                    var model = getDeliveryAddress(addressList)

                    model?.isSelectedAddress = true
                    city = model?.city
                    province = model?.state
                    country = model?.country
                    name = model?.title
                    zip = model?.pinCode
                    phone = model?.phoneNumber


                }, getDeliveryAddress(addressList)!!
            )
        }
    }

    private fun getDeliveryAddress(addressList: MutableList<UserDetailsModelClass>): UserDetailsModelClass? {
        return (addressList.find { it.isSelectedAddress }

            ?: addressList.find { it.id == ApplicationClass.defaultAdress?.id.toString() })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        //   isCancelable = false


        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =

                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            var behaviour = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            behaviour.state =
                BottomSheetBehavior.STATE_EXPANDED
            behaviour.skipCollapsed = true
            behaviour.isHideable = true
            behaviour.peekHeight = 0
//            behaviour.addBottomSheetCallback(object :
//                BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    try {
//                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//                    } catch (e: Exception) {
//                    }
//
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//                }
//            })
        }
        return bottomSheetDialog
    }


}