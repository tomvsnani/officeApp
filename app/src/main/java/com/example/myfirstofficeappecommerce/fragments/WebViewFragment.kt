package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentWebViewBinding

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
                binding!!.webviewprogressbar.visibility=View.GONE
                super.onPageFinished(view, url)
            }
        }

        setHasOptionsMenu(true)

        toolbar = binding!!.webviewToolbar

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24)

        (activity as AppCompatActivity).supportActionBar?.title =
            if (type == "checkout") "checkout" else ""

        webview!!.loadUrl(link)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.settings.loadWithOverviewMode = true;


        activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webview!!.canGoBack())
                    webview!!.goBack()
                else {
                    isEnabled = false
                    activity!!.onBackPressed()
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