package com.example.myfirstofficeappecommerce.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myfirstofficeappecommerce.*
import com.example.myfirstofficeappecommerce.Activities.MainActivity
import com.example.myfirstofficeappecommerce.databinding.FragmentProfile2Binding
import com.google.android.material.textfield.TextInputEditText
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront
import com.shopify.buy3.Storefront.*


class loginFragment(
    var signInType: String,
    var checkoutId: String = "",
    var totalTax: Float = 0f,
    var fragment: Fragment? = null
) : Fragment() {

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
        (activity as MainActivity).lockDrawer()
        setHasOptionsMenu(true)
        nameInputTxetView = binding!!.nameEditText
        phnInputTxetView = binding!!.PhonenumberEditText
        emailInputTxetView = binding!!.emailEditText
        passwordInputTxetView = binding!!.passwordEditText
        createAccountButton = binding!!.registrationSubmitButton
        signInButton = binding!!.signInButton
        progressbar = binding!!.profileProgressBar

        (activity as AppCompatActivity).setSupportActionBar(binding!!.loginToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (signInType == Constants.GUEST_SIGN_IN)
            binding!!.guestsigninTextView.visibility = View.VISIBLE
        else
            binding!!.guestsigninTextView.visibility = View.GONE

        binding!!.registrationSubmitButton2.setOnClickListener {

            it.visibility = View.GONE

        }

        binding!!.guestsigninTextView.setOnClickListener {

            (activity as MainActivity).createCheckout(Constants.GUEST_SIGN_IN)
        }



        signInButton!!.setOnClickListener {

            if (signInButton!!.text.toString().toLowerCase().contains("create")) {

                binding!!.phnEditTextWrapper.visibility = View.VISIBLE
                binding!!.nameEditTextWrapper.visibility = View.VISIBLE
                createAccountButton!!.visibility = View.VISIBLE
                binding!!.textView6.visibility = View.VISIBLE
                binding!!.registrationSubmitButton2.visibility = View.GONE
                binding!!.view5.visibility = View.VISIBLE
                signInButton!!.text = "log in"
                createAccountButton!!.text = "Create Account"
            } else {
                signInButton!!.text = "Create Account"
                createAccountButton!!.text = "sign in"
                binding!!.phnEditTextWrapper.visibility = View.GONE
                binding!!.nameEditTextWrapper.visibility = View.GONE

                binding!!.textView6.visibility = View.GONE


            }

        }
        createAccountButton!!.setOnClickListener {
            if (createAccountButton!!.text.toString().toLowerCase().contains("sign")) {

                startSignInFlow()
            } else {

                startRegistrationFlow()

            }
        }

        return view
    }

    private fun startRegistrationFlow() {
        when {
            nameInputTxetView!!.text.toString().isBlank() -> nameInputTxetView!!.error =
                getString(R.string.nameEmpty)
            emailInputTxetView!!.text.toString().isBlank() -> emailInputTxetView!!.error =
                getString(R.string.emailempty)
            passwordInputTxetView!!.text.toString()
                .isBlank() -> passwordInputTxetView!!.error =
                getString(R.string.passwordEmpty)
            phnInputTxetView!!.text.toString().isBlank() -> phnInputTxetView!!.error =
                getString(R.string.phoneEmptyError)


            !emailInputTxetView!!.text.toString()
                .contains("@") -> emailInputTxetView!!.error =
                getString(R.string.EnterValidEmail)
            passwordInputTxetView!!.text.toString().length < 5 -> passwordInputTxetView!!.error =
                getString(R.string.PasswordLengthisLess)
            phnInputTxetView!!.text.toString().length < 13 -> phnInputTxetView!!.error =
                getString(R.string.EnterPhoneNumWithCountryCode)

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

    private fun startSignInFlow() {


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
                            "Could not log you in please check your details\n " + response.errors(),
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
        ApplicationClass.signInType = Constants.NORMAL_SIGN_IN
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
                            getString(R.string.YouHaveLoggedInToast),
                            Toast.LENGTH_LONG
                        )
                            .show()

                        var sharedPref = activity!!.getPreferences(Activity.MODE_PRIVATE)
                        sharedPref.edit().putString(
                            "token",
                            response.data()!!.customerAccessTokenCreate.customerAccessToken.accessToken
                        ).apply()

                    }

                    when (fragment) {

                        is MainFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(R.id.container, MainFragment())

                            .commit()
                        is OrdersFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(
                                R.id.container,
                                OrdersFragment(ApplicationClass.selectedVariantList!!.filter {
                                    it.isOrdered
                                })
                            )

                            .commit()

                        is WishListFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(R.id.container, WishListFragment())

                            .commit()
                        is CartFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(
                                R.id.container,
                                CheckOutMainWrapperFragment(checkoutId, totalTax)
                            )

                            .commit()

                        is MyAccountFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(
                                R.id.container,
                                fragment!!
                            )

                            .commit()

                        is EditUserDetailsFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(
                                R.id.container,
                                fragment!!
                            )

                            .commit()

                        is MyAccountFragment -> activity!!.supportFragmentManager.beginTransaction()
                            .addToBackStack("login")
                            .replace(
                                R.id.container,
                                fragment!!
                            )

                            .commit()


                        else -> activity!!.supportFragmentManager.beginTransaction()

                            .replace(R.id.container, MainFragment())
                            .commit()
                    }

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

    override fun onStop() {
        super.onStop()
        var inputMethodManager: InputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding!!.root?.focusedChild?.windowToken, 0)
        binding!!.root?.focusedChild?.clearFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)

        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            (activity as MainActivity)?.supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commit()

            return true
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        Log.d("onprepareaccount", "yess")
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

}