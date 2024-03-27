package com.example.mbookie.customer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentPlayTrailerBinding
import com.example.mbookie.viewmodel.CustomerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class PlayTrailerFragment : Fragment() {

    private lateinit var binding : FragmentPlayTrailerBinding
    private val customerViewModel: CustomerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayTrailerBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        binding.webView.settings.javaScriptEnabled = true
        val youtubeVideoId = extractTextBehindLastSlash(customerViewModel.movieTrailerLink)
        val embedUrl = "https://www.youtube.com/embed/$youtubeVideoId"
        Log.d("KJHKLJHKJ", "onViewCreated: $youtubeVideoId $embedUrl")
        binding.webView.loadUrl(embedUrl)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    private fun extractTextBehindLastSlash(url: String): String {
        val parts = url.split("/")
        return parts.lastOrNull() ?: ""
    }


}