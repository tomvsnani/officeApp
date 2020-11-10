package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Constants
import com.example.myfirstofficeappecommerce.Models.UserDetailsModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentEditAddressBinding
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID


class EditAddressFragment(
    var userDetailsModelClass: UserDetailsModelClass,
    var fragment: BottomSheetFragment
) :
    BottomSheetDialogFragment() {

    var newAddressLayoutBinding: FragmentEditAddressBinding? = null
    private var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_edit_address, container, false);
        newAddressLayoutBinding = FragmentEditAddressBinding.bind(v)

        (activity as MainActivity).lockDrawer()

        setHasOptionsMenu(true)

        setUserDataToViews()

        newAddressLayoutBinding!!.addCardButton.setOnClickListener {

            changeAddress()
        }
        newAddressLayoutBinding!!.closeBottomSheetImageView.setOnClickListener { dismiss() }

        return v
    }

    private fun changeAddress() {
        if (isUserEnteredRequiedData()
        ) {
            newAddressLayoutBinding!!.editaddressprogressbar.visibility = View.VISIBLE
            val input = getMainAddressInput()

            val query = customerAddressUpdateQuery(input)

            var call =
                CategoriesDataProvider.graphh!!.mutateGraph(query)

            call.enqueue(object : GraphCall.Callback<Mutation> {

                override fun onResponse(response: GraphResponse<Mutation>) {
                    Log.d("addressup", response.errors().toString())

                    activity!!.runOnUiThread {
                        newAddressLayoutBinding!!.editaddressprogressbar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Address updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (ApplicationClass.addresstype != Constants.ADD_ADDRESS_TYPE_USER_ADDRESS) {
                            for (i in parentFragment!!.childFragmentManager.fragments) {

                                if (i is FinalisingOrderFragment)
                                    (i as FinalisingOrderFragment).apply {
                                        ApplicationClass.addressList.filter {
                                            if (it.isSelectedAddress)
                                                it.isSelectedAddress = false
                                            return@filter true
                                        }
                                        userDetailsModelClass.isSelectedAddress = true

                                        retrieve_addresses()


                                    }



                            }
                        }
                        try {
                            fragment.dismiss()
                            dismiss()
                        } catch (e: Exception) {
                        }
                    }
                }


                override fun onFailure(error: GraphError) {
                    activity!!.runOnUiThread {
                        newAddressLayoutBinding!!.editaddressprogressbar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Address not updated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        } else Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
    }

    private fun customerAddressUpdateQuery(input: MailingAddressInput?): MutationQuery? {
        return mutation { mutationQuery: MutationQuery ->
            mutationQuery
                .customerAddressUpdate(
                    activity!!.getPreferences(Activity.MODE_PRIVATE)
                        .getString("token", ""), ID(userDetailsModelClass.id), input
                ) { _queryBuilder ->
                    _queryBuilder.customerAddress { _queryBuilder ->

                    }
                        .userErrors { _queryBuilder -> _queryBuilder.field().message() }
                }

        }
    }

    private fun getMainAddressInput(): MailingAddressInput? {
        userDetailsModelClass.city = newAddressLayoutBinding!!.cityEditText.text.toString()
        userDetailsModelClass.phoneNumber =
            newAddressLayoutBinding!!.PhonenumberEditText.text.toString()
        userDetailsModelClass.country = newAddressLayoutBinding!!.countryEditText.text.toString()
        userDetailsModelClass.title = newAddressLayoutBinding!!.nameEditText.text.toString()
        userDetailsModelClass.subTitle = newAddressLayoutBinding!!.lastnameEditText.text.toString()
        userDetailsModelClass.state = newAddressLayoutBinding!!.provinceEditText.text.toString()
        return MailingAddressInput()
            .setAddress1(newAddressLayoutBinding!!.cityEditText.text.toString())
            .setCity(newAddressLayoutBinding!!.cityEditText.text.toString())
            .setFirstName(newAddressLayoutBinding!!.nameEditText.text.toString())
            .setPhone(newAddressLayoutBinding!!.PhonenumberEditText.text.toString())
            .setProvince(newAddressLayoutBinding!!.provinceEditText.text.toString())
            .setZip(newAddressLayoutBinding!!.zipEditText.text.toString())
            .setLastName(newAddressLayoutBinding!!.lastnameEditText.text.toString())
            .setCountry(newAddressLayoutBinding!!.countryEditText.text.toString())
    }

    private fun isUserEnteredRequiedData(): Boolean {
        return newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank()
    }

    private fun setUserDataToViews() {
        newAddressLayoutBinding!!.cityEditText.setText(userDetailsModelClass.city)

        newAddressLayoutBinding!!.nameEditText.setText(userDetailsModelClass.title)
        newAddressLayoutBinding!!.provinceEditText.setText(userDetailsModelClass.state)
        newAddressLayoutBinding!!.zipEditText.setText(userDetailsModelClass.pinCode)
        newAddressLayoutBinding!!.lastnameEditText.setText(userDetailsModelClass.subTitle)
        newAddressLayoutBinding!!.countryEditText.setText(userDetailsModelClass.country)
        newAddressLayoutBinding!!.emailEditText.setText(userDetailsModelClass.email)
        newAddressLayoutBinding!!.PhonenumberEditText.setText(userDetailsModelClass.phoneNumber)
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