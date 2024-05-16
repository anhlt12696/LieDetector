package com.example.liedetector.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.base.BaseActivity
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityTipsBinding


class TipsActivity : BaseActivity() {
    private lateinit var binding: ActivityTipsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)




//        appOpenManager = AppOpenManager(this)
//        appOpenManager.loadAppOpenAds(this)
//        appOpenManager.showAd(this)

        binding.btnUnderstand.setOnClickListener {
            val intent = Intent(this@TipsActivity,HomeActivity::class.java)
            startActivity(intent)
        }

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        val content1: String? = getColoredSpanned(resources.getString(R.string.tip_to_play_content_1), "#ffffff")
        val Truth: String? = getColoredSpanned(resources.getString(R.string.tip_to_play_content_truth), "#1BC522")
        val Lie: String? = getColoredSpanned(resources.getString(R.string.tip_to_play_content_lie), "#FF644F")
        val content2: String? = getColoredSpanned(resources.getString(R.string.tip_to_play_content_2), "#ffffff")

        binding.tvTipsContent.text = Html.fromHtml("$content1 $Truth$content2 $Lie")

    }

    fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
}