package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.MainActivity
import com.example.myfirstofficeappecommerce.Models.ModelClass
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.NewAddressLayoutBinding
import com.shopify.buy3.*
import com.shopify.buy3.Storefront.*
import com.shopify.graphql.support.ID
import java.util.concurrent.TimeUnit


class EditAddressFragment(var modelClass: ModelClass) : Fragment() {

    var newAddressLayoutBinding: NewAddressLayoutBinding? = null
    private var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_edit_address, container, false);
        newAddressLayoutBinding = NewAddressLayoutBinding.bind(v)
        (activity as MainActivity).lockDrawer()

        toolbar = v?.findViewById(R.id.newaddressToolbar)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        newAddressLayoutBinding!!.cityEditText.setText(modelClass.city)

        newAddressLayoutBinding!!.nameEditText.setText(modelClass.title)
        newAddressLayoutBinding!!.provinceEditText.setText(modelClass.state)
        newAddressLayoutBinding!!.zipEditText.setText(modelClass.pinCode)
        newAddressLayoutBinding!!.lastnameEditText.setText(modelClass.subTitle)
        newAddressLayoutBinding!!.countryEditText.setText(modelClass.country)

        newAddressLayoutBinding!!.addAddressButton.setOnClickListener {

            if (newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.cityEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.nameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.provinceEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.zipEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.lastnameEditText.text.toString().isNotBlank() &&
                newAddressLayoutBinding!!.countryEditText.text.toString().isNotBlank()
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
                                .getString("token", ""), ID(modelClass.id), input
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
}