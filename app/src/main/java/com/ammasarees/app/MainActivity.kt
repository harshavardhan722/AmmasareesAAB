package com.ammasarees.app

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var offlineLayout: LinearLayout
    private var webUrl = "https://timeless-sarees.lovable.app"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make status bar transparent / colored
        window.statusBarColor = Color.parseColor("#7C3AED") // Purple to match saree site

        setContentView(R.layout.activity_main)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        webView = findViewById(R.id.webView)
        offlineLayout = findViewById(R.id.offlineLayout)
        val retryButton = findViewById<android.widget.Button>(R.id.retryButton)

        setupWebView()
        setupSwipeRefresh()

        retryButton.setOnClickListener {
            if (isNetworkAvailable()) {
                offlineLayout.visibility = View.GONE
                webView.visibility = View.VISIBLE
                webView.reload()
            } else {
                Toast.makeText(this, "Still no internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        if (isNetworkAvailable()) {
            webView.loadUrl(webUrl)
        } else {
            showOffline()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        settings.setSupportZoom(false)
        settings.allowFileAccess = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        settings.userAgentString = settings.userAgentString + " AmmaSareesApp/1.0"

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView, url: String) {
                swipeRefresh.isRefreshing = false
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                if (request.isForMainFrame) {
                    swipeRefresh.isRefreshing = false
                    if (!isNetworkAvailable()) {
                        showOffline()
                    }
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                // Keep navigation within the app for the same domain
                return if (url.contains("lovable.app") || url.contains("timeless-sarees")) {
                    view.loadUrl(url)
                    false
                } else {
                    // Open external links in browser
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                    startActivity(intent)
                    true
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    swipeRefresh.isRefreshing = false
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                // Could update toolbar title here if needed
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(
            Color.parseColor("#7C3AED"),
            Color.parseColor("#EC4899"),
            Color.parseColor("#F59E0B")
        )
        swipeRefresh.setOnRefreshListener {
            if (isNetworkAvailable()) {
                offlineLayout.visibility = View.GONE
                webView.visibility = View.VISIBLE
                webView.reload()
            } else {
                swipeRefresh.isRefreshing = false
                showOffline()
            }
        }
    }

    private fun showOffline() {
        webView.visibility = View.GONE
        offlineLayout.visibility = View.VISIBLE
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }
}
