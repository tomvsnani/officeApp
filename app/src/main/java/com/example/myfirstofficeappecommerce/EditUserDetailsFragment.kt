package com.example.myfirstofficeappecommerce

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.databinding.FragmentEditUserDetailsBinding
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront


class EditUserDetailsFragment : Fragment() {
    var binding: FragmentEditUserDetailsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("clicked","back")
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            Log.d("clicked","back1")
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_edit_user_details, container, false)
        binding = FragmentEditUserDetailsBinding.bind(view)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding!!.editprofileToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        getUserData()

        binding!!.savedetailsbutton.setOnClickListener {
            updateUserData()
        }
        return view
    }

    private fun updateUserData() {
        binding!!.errortextview.text = ""
        var inputMethodManager: InputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding!!.root.focusedChild.windowToken, 0)
        binding!!.root.focusedChild.clearFocus()
        var customerUpdateInput = Storefront.CustomerUpdateInput()
        when {
            binding!!.firstnameedittext.text.toString()
                .isBlank() -> binding!!.firstnameedittext.error = "Please enter firstname"
            binding!!.lastnameedittext.text.toString()
                .isBlank() -> binding!!.lastnameedittext.error = "Please enter lastname"
            binding!!.phnnumedittext.text.toString()
                .isBlank() -> binding!!.phnnumedittext.error = "Please enter phone number"
            !binding!!.emailedittext.text.toString()
                .contains("@") -> binding!!.emailedittext.error = "Please enter a valid email"
            binding!!.emailedittext.text.toString().isBlank() -> binding!!.emailedittext.error =
                "Pleas enter email"
        }
        customerUpdateInput.apply {
            this.firstName = binding!!.firstnameedittext.text.toString()
            this.lastName = binding!!.lastnameedittext.text.toString()
            this.phone = binding!!.phnnumedittext.text.toString()
        }
        var updateUserDetailsQuery = Storefront.mutation { _queryBuilder ->
            _queryBuilder.customerUpdate(
                (activity!!.application as ApplicationClass).getCustomerToken(
                    activity as MainActivity
                ),
                customerUpdateInput
            ) { _queryBuilder ->
                _queryBuilder.userErrors { _queryBuilder ->
                    _queryBuilder.message().field()

                }.customer { _queryBuilder ->
                    _queryBuilder.displayName()
                }
            }

        }

        var call = CategoriesDataProvider.graphh!!.mutateGraph(updateUserDetailsQuery)
        call.enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if (response.data() != null && response.data()!!.customerUpdate.userErrors.isEmpty())
                    activity?.runOnUiThread {
                        Toast.makeText(
                            context,
                            "Changes have been saved",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                else {
                    if (response.data()!!.customerUpdate.userErrors.isNotEmpty()) {
                        var s = ""
                        for (i in response.data()!!.customerUpdate.userErrors)
                            s = s + i.message + " \n"
                        binding!!.errortextview.post {
                            binding!!.errortextview.text = s
                        }
                    }
                }
            }


            override fun onFailure(error: GraphError) {
                Toast.makeText(
                    context,
                    "Sorry could not update changes",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getUserData() {
        var retrieveUserDetailsQuery = Storefront.query { _queryBuilder ->
            _queryBuilder.customer(
                (activity!!.application as ApplicationClass).getCustomerToken(
                    activity as MainActivity
                )
            ) { _queryBuilder ->
                _queryBuilder.email().firstName().lastName().phone()
            }
        }
        binding!!.savedetailsbutton.isClickable = false
        binding!!.editDetailsProgressbar.visibility = View.VISIBLE
        var call1 = CategoriesDataProvider.graphh!!.queryGraph(retrieveUserDetailsQuery)
        call1.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                if (response.data() != null) {
                    Log.d("updated", response.errors().size.toString())
                    activity?.runOnUiThread {
                        binding!!.firstnameedittext.setText(response.data()!!.customer.firstName.toString())
                        binding!!.lastnameedittext.setText(response.data()!!.customer.lastName.toString())
                        binding!!.emailedittext.setText(response.data()!!.customer.email)
                        binding!!.phnnumedittext.setText(response.data()!!.customer.phone)
                        binding!!.savedetailsbutton.isClickable = true
                        binding!!.editDetailsProgressbar.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(error: GraphError) {
                Log.d("updated", error.message.toString())
            }
        })
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }


}