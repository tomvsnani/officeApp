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


class EditAddressFragment(var userDetailsModelClass: UserDetailsModelClass) :
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



        newAddressLayoutBinding!!.cityEditText.setText(userDetailsModelClass.city)

        newAddressLayoutBinding!!.nameEditText.setText(userDetailsModelClass.title)
        newAddressLayoutBinding!!.provinceEditText.setText(userDetailsModelClass.state)
        newAddressLayoutBinding!!.zipEditText.setText(userDetailsModelClass.pinCode)
        newAddressLayoutBinding!!.lastnameEditText.setText(userDetailsModelClass.subTitle)
        newAddressLayoutBinding!!.countryEditText.setText(userDetailsModelClass.country)
        newAddressLayoutBinding!!.emailEditText.setText(userDetailsModelClass.email)

        newAddressLayoutBinding!!.addCardButton.setOnClickListener {

            if (newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.emailEditText.text.toString().contains("@")
            ) {

                val input = MailingAddressInput()
                    .setAddress1(newAddressLayoutBinding!!.cityEditText.text.toString())
                    .setCity(newAddressLayoutBinding!!.cityEditText.text.toString())
                    .setFirstName(newAddressLayoutBinding!!.nameEditText.text.toString())
                    .setPhone(newAddressLayoutBinding!!.PhonenumberEditText.text.toString())
                    .setProvince(newAddressLayoutBinding!!.provinceEditText.text.toString())
                    .setZip(newAddressLayoutBinding!!.zipEditText.text.toString())
                    .setLastName(newAddressLayoutBinding!!.lastnameEditText.text.toString())
                    .setCountry(newAddressLayoutBinding!!.countryEditText.text.toString())

                Log.d(
                    "customerq", activity!!.getPreferences(Activity.MODE_PRIVATE)
                        .getString("checkoutid", "")!!
                )
                val query = mutation { mutationQuery: MutationQuery ->
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


                var call =
                    CategoriesDataProvider.graphh!!.mutateGraph(query)
                call.enqueue(object : GraphCall.Callback<Storefront.Mutation> {


                    override fun onResponse(response: GraphResponse<Mutation>) {
                        Log.d("addressup", response.errors().toString())

                        activity!!.runOnUiThread {
                            Toast.makeText(
                                context,
                                "Address updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    override fun onFailure(error: GraphError) {
                        activity!!.runOnUiThread {
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

        return v
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