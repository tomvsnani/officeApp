package com.example.myfirstofficeappecommerce.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.fragments.BottomSheetFragment
import com.example.myfirstofficeappecommerce.fragments.EditAddressFragment
import com.example.myfirstofficeappecommerce.fragments.NewAddressFragment
import kotlin.math.max


class ChooseAddressRecyclerAdapter(var context: BottomSheetFragment, var checkoutId: String) :
    ListAdapter<UserDetailsModelClass, ChooseAddressRecyclerAdapter.ChooseAddressViewHolder>(
        UserDetailsModelClass.DIFF_UTIL
    ) {
    inner class ChooseAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ItemNameTextView: TextView? = itemView.findViewById(R.id.chooseAddressnameTextView)
        var LocationNameTextView: TextView? =
            itemView.findViewById(R.id.chooseAddressaddressTextView)

        var phnNumTextView: TextView? = itemView.findViewById(R.id.chooseAddressPhoneNumber)
        var editButton: Button? = itemView.findViewById(R.id.chooseAddressEditButton)
        var radioButton: RadioButton? = itemView.findViewById(R.id.radioButton)
        var root: ConstraintLayout = itemView.findViewById(R.id.addressroot)

        init {

            editButton?.setOnClickListener {

                if (ApplicationClass.addresstype == Constants.ADD_ADDRESS_TYPE_ORDER_ADDRESS)
                    EditAddressFragment(
                        currentList[absoluteAdapterPosition],
                        (context as BottomSheetFragment)
                    ).show(
                        (context as BottomSheetFragment).parentFragment?.childFragmentManager!!,
                        ""
                    )
                else
                    EditAddressFragment(
                        currentList[absoluteAdapterPosition],
                        (context as BottomSheetFragment)
                    ).show(
                        (context as BottomSheetFragment).activity?.supportFragmentManager!!,
                        ""
                    )


            }

            radioButton!!.setOnClickListener {
                var modelclass = currentList[absoluteAdapterPosition]

                currentList.filter {
                    if (it.isSelectedAddress)
                        it.isSelectedAddress = false
                    return@filter true
                }
                modelclass.isSelectedAddress = true
                notifyDataSetChanged()


            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAddressViewHolder {
        return ChooseAddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.choose_shipping_address_row_layout, parent, false)
        )

    }

    override fun submitList(list: MutableList<UserDetailsModelClass>?) {

        super.submitList(list!!.toList())

    }

    override fun onBindViewHolder(holder: ChooseAddressViewHolder, position: Int) {
        var userDetailsModelClass: UserDetailsModelClass = currentList[position]

        if (ApplicationClass.addresstype == Constants.ADD_ADDRESS_TYPE_USER_ADDRESS)
            holder.radioButton?.visibility = View.GONE
        else
            holder.radioButton?.visibility = View.VISIBLE

        holder.radioButton!!.isChecked = userDetailsModelClass.isSelectedAddress
        holder.ItemNameTextView?.text = userDetailsModelClass.title

        holder.LocationNameTextView?.text =
            userDetailsModelClass.hnum + " ," + userDetailsModelClass.city + " \n\n" + userDetailsModelClass.state + " , " + userDetailsModelClass.pinCode

        holder.phnNumTextView?.text = userDetailsModelClass.phoneNumber
        Log.d(
            "applicationaddresssize",
            currentList.size.toString()
        )

    }

    override fun getItemCount(): Int {
        return max(0, currentList.size)
    }
}