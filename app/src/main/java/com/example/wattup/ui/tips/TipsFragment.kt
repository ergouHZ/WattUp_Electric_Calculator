package com.example.wattup.ui.tips

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.example.wattup.R
import com.example.wattup.ui.tips.TipsViewModel
import com.example.wattup.utils.PreferenceManager

class TipsFragment : Fragment() {

    companion object {
        fun newInstance() = TipsFragment()
    }

    private lateinit var viewModel: TipsViewModel
    private lateinit var webViewTwitter: WebView
    private lateinit var webViewVideo: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_tips, container, false)
        webViewTwitter = rootView.findViewById(R.id.webView)
        webViewVideo = rootView.findViewById(R.id.video_youtube)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TipsViewModel::class.java)




    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        val webSettings = webViewTwitter.settings
        val webSettings2 = webViewVideo.settings
        webSettings.javaScriptEnabled = true
        webSettings2.javaScriptEnabled = true

        webViewTwitter.webViewClient = WebViewClient()
        val twitterTimelineHtml = """
            <html>
                <body>
                    <a class="twitter-timeline" href="https://twitter.com/EU_ENV?ref_src=twsrc%5Etfw">Tweets by EU_ENV</a> 
                    <script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
                </body>
            </html>
        """
        webViewTwitter.loadData(twitterTimelineHtml, "text/html", "UTF-8")

        webViewVideo.webViewClient = WebViewClient()
        val videoUrl = "https://www.youtube.com/embed/C6Ua77yewdY?si=YZRPPk973HD5paiX"
        webViewVideo.loadUrl(videoUrl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWebView() // 调用设置 WebView 的方法
        val username = context?.let { PreferenceManager.getUserName(it) }
//        val nameText :TextView = view.findViewById(R.id.userName)
//        nameText.text = username
    }
}