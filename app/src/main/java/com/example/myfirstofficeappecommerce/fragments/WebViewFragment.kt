package com.example.myfirstofficeappecommerce.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.example.myfirstofficeappecommerce.R
import com.example.myfirstofficeappecommerce.databinding.FragmentWebViewBinding

class WebViewFragment(var link:String) : Fragment() {

    var binding:FragmentWebViewBinding?=null
    var webview:WebView?=null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentWebViewBinding.inflate(layoutInflater)
        webview=binding!!.webviewfragWebview
        webview!!.webViewClient= object : WebViewClient() {

        }
        webview!!.loadUrl(link)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.settings.loadWithOverviewMode = true;

        activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              if(webview!!.canGoBack())
                  webview!!.goBack()
                else {
                  isEnabled=false
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

}