package org.map_bd.surveycalculator


import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityWebviewsBinding
import java.io.BufferedReader
import java.io.InputStreamReader


class WebviewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val webView = findViewById<WebView>(R.id.webViews)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // Read the HTML content from res/raw/index.html
        val htmlContent = readRawHtmlFile(R.raw.files)

        // Load the string data into the WebView
        webView.loadDataWithBaseURL("file:///android_res/raw/", htmlContent, "text/html", "UTF-8", null)
    }

    // Helper function to convert raw resource stream to String
    private fun readRawHtmlFile(resourceId: Int): String {
        val inputStream = resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}