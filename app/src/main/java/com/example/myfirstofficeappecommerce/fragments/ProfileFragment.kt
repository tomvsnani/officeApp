package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.CategoriesDataProvider
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentProfile2Binding
import com.google.android.material.textfield.TextInputEditText
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.buy3.Storefront.*


class ProfileFragment : Fragment() {

    var nameInputTxetView: TextInputEditText? = null
    var phnInputTxetView: TextInputEditText? = null
    var emailInputTxetView: TextInputEditText? = null
    var passwordInputTxetView: TextInputEditText? = null
    var createAccountButton: Button? = null
    var binding: FragmentProfile2Binding? = null
    var isRegistered: Boolean = false
    var tempPassword: String? = null
    var signInButton: Button? = null
    var progressbar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile2, container, false)
        binding = FragmentProfile2Binding.bind(view)
        nameInputTxetView = binding!!.nameEditText
        phnInputTxetView = binding!!.PhonenumberEditText
        emailInputTxetView = binding!!.emailEditText
        passwordInputTxetView = binding!!.passwordEditText
        createAccountButton = binding!!.registrationSubmitButton
        signInButton = binding!!.signInButton
        progressbar = binding!!.profileProgressBar



        binding!!.registrationSubmitButton2.setOnClickListener {
            binding!!.phnEditTextWrapper.visibility = View.VISIBLE
            binding!!.nameEditTextWrapper.visibility = View.VISIBLE
            createAccountButton!!.visibility = View.VISIBLE
            binding!!.textView6.visibility = View.VISIBLE
            binding!!.registrationSubmitButton2.visibility = View.GONE
            binding!!.view5.visibility = View.VISIBLE
            it.visibility = View.GONE

        }



        signInButton!!.setOnClickListener {

            binding!!.phnEditTextWrapper.visibility = View.GONE
            binding!!.nameEditTextWrapper.visibility = View.GONE
            createAccountButton!!.visibility = View.GONE
            binding!!.textView6.visibility = View.GONE
            binding!!.registrationSubmitButton2.visibility = View.VISIBLE
            binding!!.view5.visibility = View.GONE


            progressbar!!.visibility = View.VISIBLE
            if (emailInputTxetView!!.text.toString()
                    .isNotBlank() && passwordInputTxetView!!.text.toString().isNotBlank()
            ) {
                login(
                    emailInputTxetView!!.text.toString(),
                    passwordInputTxetView!!.text.toString()
                )

            } else {
                progressbar!!.visibility = View.GONE
                Toast.makeText(
                    context,
                    getString(R.string.loginErrorMessgae),
                    Toast.LENGTH_LONG
                )
                    .show()

            }

        }



        createAccountButton!!.setOnClickListener {

            when {
                nameInputTxetView!!.text.toString().isBlank() -> nameInputTxetView!!.error =
                    "Name cannot be empty"
                emailInputTxetView!!.text.toString().isBlank() -> emailInputTxetView!!.error =
                    "Email cannot be empty"
                passwordInputTxetView!!.text.toString().isBlank() -> passwordInputTxetView!!.error =
                    "password cannot be empty"
                phnInputTxetView!!.text.toString().isBlank() -> phnInputTxetView!!.error =
                    "phone cannot be empty"


                !emailInputTxetView!!.text.toString().contains("@") -> emailInputTxetView!!.error =
                    "Please enter a valid email address"
                passwordInputTxetView!!.text.toString().length < 5 -> passwordInputTxetView!!.error =
                    "password length should be greater than 5 "
                phnInputTxetView!!.text.toString().length < 13 -> phnInputTxetView!!.error =
                    "phone enter the phone number wih country code "

                else -> {
                    createAccountButton!!.isClickable = false
                    signInButton!!.isClickable = false
                    createAccount()
                    var a = object : CountDownTimer(2000, 1) {
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            createAccountButton!!.isClickable = true
                            signInButton!!.isClickable = true
                        }

                    }.start()

                }


            }


        }

        return view
    }


    fun createAccount() {
        progressbar!!.visibility = View.VISIBLE
        val input = CustomerCreateInput(
            emailInputTxetView!!.text.toString(),
            passwordInputTxetView!!.text.toString()
        )
            .setFirstName(nameInputTxetView!!.text.toString())

            .setPhone(phnInputTxetView!!.text.toString())

        tempPassword = passwordInputTxetView!!.text.toString()


        val mutationQuery = mutation { mutation: MutationQuery ->
            mutation
                .customerCreate(
                    input
                ) { query: CustomerCreatePayloadQuery ->
                    query
                        .customer { customer: CustomerQuery ->
                            customer

                                .email()
                                .firstName()

                        }
                        .userErrors { userError: UserErrorQuery ->
                            userError
                                .field()
                                .message()
                        }
                }
        }

        var calldata = CategoriesDataProvider.graphh!!.mutateGraph(mutationQuery)


        calldata.enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Mutation>) {
                var a = response.data()!!.customerCreate


                if (a != null) {
                    if (a.userErrors.isEmpty()) {

                        login(a.customer.email, tempPassword!!)

                    } else {
                        for (i in a.userErrors)

                            activity!!.runOnUiThread {
                                progressbar!!.visibility = View.GONE
                                Toast.makeText(context, i.message, Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {

                    activity!!.runOnUiThread {
                        progressbar!!.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Could not log you in please check your details\n ${response.errors()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }

            override fun onFailure(error: GraphError) {

            }
        })


    }

    private fun login(email: String, password: String) {
        activity!!.runOnUiThread {
            progressbar!!.visibility = View.VISIBLE
        }
        val input = CustomerAccessTokenCreateInput(email, password)
        val mutationQuery = mutation { mutation: MutationQuery ->
            mutation
                .customerAccessTokenCreate(
                    input
                ) { query: CustomerAccessTokenCreatePayloadQuery ->
                    query
                        .customerAccessToken { customerAccessToken: CustomerAccessTokenQuery ->
                            customerAccessToken
                                .accessToken()
                                .expiresAt()
                        }
                        .userErrors { userError: UserErrorQuery ->
                            userError
                                .field()
                                .message()
                        }
                }
        }

        var calldata = CategoriesDataProvider.graphh!!.mutateGraph(mutationQuery)


        calldata.enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Mutation>) {
                var a = response.data()!!.customerAccessTokenCreate.userErrors.isNullOrEmpty()
                if (a) {
                    activity!!.runOnUiThread {
                        Toast.makeText(
                            context,
                            "You have successfully logged in",
                            Toast.LENGTH_LONG
                        )
                            .show()

                        var sharedPref = activity!!.getPreferences(Activity.MODE_PRIVATE)
                        sharedPref.edit().putString(
                            "token",
                            response.data()!!.customerAccessTokenCreate.customerAccessToken.accessToken
                        ).apply()

                    }

                    Log.d(
                        "customertoken",
                        response.data()!!.customerAccessTokenCreate.customerAccessToken.accessToken + "  " + response.data()!!.customerAccessTokenCreate.customerAccessToken.expiresAt
                    )

                    activity!!.supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container, MainFragment())
                        .addToBackStack(null)
                        .commit()

                } else {
                    activity!!.runOnUiThread { progressbar!!.visibility = View.GONE }
                    for (i in response.data()!!.customerAccessTokenCreate.userErrors)
                        activity!!.runOnUiThread {
                            Toast.makeText(
                                context,
                                "There is some problem logging you in . \n ${i.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }


            }

            override fun onFailure(error: GraphError) {

            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)

        super.onCreate(savedInstanceState)
    }



}