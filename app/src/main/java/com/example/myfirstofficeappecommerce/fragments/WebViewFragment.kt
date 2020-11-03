package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstofficeappecommerce.ApplicationClass
import com.example.myfirstofficeappecommerce.Database.MyDatabase
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentWebViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebViewFragment(var link: String, var type: String) : Fragment() {

    var binding: FragmentWebViewBinding? = null
    var webview: WebView? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(layoutInflater)
        webview = binding!!.webviewfragWebview
        webview!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("pageurl", url!!)
                binding!!.webviewprogressbar.visibility = View.GONE

                if (url!!.contains("thank_you")) {
                    binding!!.stepinclude.checkoutTextView3.apply {
                        setTextColor(Color.WHITE)
                        setBackground(resources.getDrawable(R.drawable.circle_background_drawable_highlighted))
                        var builder = AlertDialog.Builder(context!!)

                        var alert = builder.create()
                        alert.setMessage("Your order has been placed successfully")
                        alert.setButton(
                            AlertDialog.BUTTON_POSITIVE, ""
                        ) { p0, p1 -> }
                        alert.show()
                        Thread.sleep(2000)
                        webview!!.clearHistory()
                        webview = null
                        (parentFragment as CheckOutMainWrapperFragment).clearAllFragmets()
//                        parentFragment!!.childFragmentManager.popBackStackImmediate(
//                            null,
//                            FragmentManager.POP_BACK_STACK_INCLUSIVE
//                        )


                        CoroutineScope(Dispatchers.IO).launch {
                            if (ApplicationClass.selectedVariantList
                                !!.find { it.isfav } != null
                            )
                                MyDatabase.getDbInstance(context!!).dao()
                                    .delete(
                                        ApplicationClass.selectedVariantList
                                        !!.find { it.isfav }!!
                                    )
                            ApplicationClass.selectedVariantList!!.clear()
                        }
                    }
                }
                super.onPageFinished(view, url)
            }
        }

        setHasOptionsMenu(true)

        toolbar = binding!!.webviewToolbar

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        if (parentFragment!!.childFragmentManager.backStackEntryCount == 0) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)
        } else
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        (activity as AppCompatActivity).supportActionBar?.title =
            if (type == "checkout") "checkout" else ""

        webview!!.loadUrl(link)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.settings.loadWithOverviewMode = true;

        if (activity != null)
            activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {


                    if (webview != null && webview!!.canGoBack()) {

                        webview!!.goBack()
                    } else {
                        if (parentFragment!!.childFragmentManager.backStackEntryCount > 1) {

                                parentFragment!!.childFragmentManager.popBackStackImmediate()

                                isEnabled = false
                                activity?.onBackPressed()

                        }
                        else
                            isEnabled=false
                    }
                }

            })

        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = TransitionInflater.from(context)
        enterTransition = inflater.inflateTransition(R.transition.fragment_slide_anim)
        exitTransition = inflater.inflateTransition(R.transition.fragment_fade_trans)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
//            if(parentFragment!!.childFragmentManager.backStackEntryCount>=0)
//                parentFragment!!.childFragmentManager.popBackStackImmediate()
//            else
            activity!!.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        for (i in 0 until menu.size())
            menu.getItem(i).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

}