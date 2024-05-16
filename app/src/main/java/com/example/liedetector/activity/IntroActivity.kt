package com.example.liedetector.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.liedetector.adapter.IntroAdapter
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.getPreLanguageName
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.base.MyApplication.setPreLanguage
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityIntroBinding
import java.util.Locale

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var language: String? = null
        var language_name: String = ""
        language = this.getPreLanguage(this)
        Log.d("Language__in intro","language in onCreate lang " + language.toString())
        language_name = getPreLanguageName(this).toString()
        Log.d("Language__in intro", "language in onCreate lang name $language_name")

        val pagerAdapter = IntroAdapter(this)
        binding.viewPager2.adapter = pagerAdapter
        binding.dotsIndicator.setViewPager2(binding.viewPager2)

     //   AppLovinManager.showAd()
        val viewPagerPageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Handle page selection here
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        }

        binding.viewPager2.registerOnPageChangeCallback(viewPagerPageChangeListener)
        binding.btnNext.setOnClickListener {
            val currentPos = binding.viewPager2.currentItem
            if (currentPos != 1) {
                binding.viewPager2.currentItem = currentPos + 1
            } else {
                val intent = Intent(this@IntroActivity, TipsActivity::class.java)
                startActivity(intent)

            }
        }
        }

    }
