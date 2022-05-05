package com.example.mynewsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class storyWebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_web_view)

        val myUrl = intent.extras?.getString("URL")
        var webView = findViewById<WebView>(R.id.myWebView)

        webView.settings.javaScriptEnabled = true
        if (myUrl != null) {
            webView.loadUrl(myUrl)
        }
    }
}