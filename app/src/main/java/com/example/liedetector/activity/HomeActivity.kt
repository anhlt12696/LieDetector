package com.example.liedetector.activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.fragment.EyeFragment
import com.example.liedetector.fragment.FingerFragment
import com.example.liedetector.fragment.TwoPlayerFragment
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private var binding: ActivityHomeBinding? = null
    private var fragmentManager: FragmentManager? = null
    private var navTab: String = "one_player"
    var result: Int? = null

    private val handler = Handler(Looper.getMainLooper())
    lateinit var adView: AdView
    lateinit var adRequest: AdRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.imgHomeLanguage.setOnClickListener {
            val home_language = true
            val intent = Intent(this@HomeActivity, LanguageActivity::class.java)
            intent.putExtra("home_language", home_language)
            startActivity(intent)
        }

        binding!!.imgHomeTip.setOnClickListener {
            val intent = Intent(this@HomeActivity, TipsActivity::class.java)
            startActivity(intent)
        }
        fragmentManager = supportFragmentManager

        navTab = Common.getGameplay(this)!!
        Log.d("check_tab", navTab)
        bottomNavigationClick()
        changeFragment()

        result = null

       // MobileAds.initialize(this)

     //   AdsManager.initAds(this)
      //  AdsManager.loadBannerAds(this,binding!!.bannerHome)



        val lie: String? = getColoredSpanned("LIE", "#83FFF8")
        val detector: String? = getColoredSpanned("DETECTOR", "#FFFFFF")


        binding!!.tvTitle.text = Html.fromHtml("$lie $detector")

    }

    fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

    private fun loadBannerAds() {
        adView = AdView(this)
        adView.adUnitId = "ca-app-pub-3940256099942544/2014213617"

        val adSize = adSize
        adView.setAdSize(adSize)

        val extras = Bundle()
        extras.putString("collapsible", "bottom")

        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()

        binding!!.bannerHome.addView(adView)
        adView.loadAd(adRequest)
        handler.postDelayed({
            refreshBannerAd()
        }, 10000)
    }

    private fun refreshBannerAd() {
        val extras = Bundle()
        extras.putString("collapsible", "bottom")

        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        adView.loadAd(adRequest)

        // Lên lịch làm mới quảng cáo tiếp theo
        handler.postDelayed({
            refreshBannerAd()
        }, 10000)
    }

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding!!.bannerHome.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    private fun bottomNavigationClick() {
        binding!!.btnFinger.setOnClickListener {
            navTab = "one_player"
            Log.d("test", "finger$navTab")
            Common.setGamePlay(this, navTab)
            changeFragment()
        }

        binding!!.btn2Finger.setOnClickListener {
            navTab = "two_player"
            Log.d("test", "2player$navTab")
            Common.setGamePlay(this, navTab)
            changeFragment()
        }

        binding!!.btnEye.setOnClickListener {
            navTab = "eye"
            Log.d("test", "eye$navTab")
            Common.setGamePlay(this, navTab)
            changeFragment()
        }
    }

    private fun changeFragment() {

        when (navTab) {
            "one_player" -> {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentMain, FingerFragment())
                    ?.commit()

                binding!!.bottomNav.setBackgroundResource(R.drawable.test_bg_finger)
                binding!!.imgFinger.visibility = View.VISIBLE
                binding!!.tvFingerprint.visibility = View.VISIBLE
                binding!!.img2Player.visibility = View.INVISIBLE
                binding!!.tv2Player.visibility = View.INVISIBLE
                binding!!.imgEye.visibility = View.INVISIBLE
                binding!!.tvEye.visibility = View.INVISIBLE
                AdsManager.loadBannerCollapsibleAds(this,binding!!.bannerHome)
                setLocale(this,getPreLanguage(this))
            }

            "two_player" -> {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentMain, TwoPlayerFragment())
                    ?.commit()

                binding!!.bottomNav.setBackgroundResource(R.drawable.test_bg_2_player)
                binding!!.imgFinger.visibility = View.INVISIBLE
                binding!!.tvFingerprint.visibility = View.INVISIBLE
                binding!!.img2Player.visibility = View.VISIBLE
                binding!!.tv2Player.visibility = View.VISIBLE
                binding!!.imgEye.visibility = View.INVISIBLE
                binding!!.tvEye.visibility = View.INVISIBLE
                AdsManager.loadBannerCollapsibleAds(this,binding!!.bannerHome)
                setLocale(this,getPreLanguage(this))
            }

            "eye" -> {
                fragmentManager?.beginTransaction()?.replace(R.id.fragmentMain, EyeFragment())
                    ?.commit()

                binding!!.bottomNav.setBackgroundResource(R.drawable.test_bg_eye)
                binding!!.imgFinger.visibility = View.INVISIBLE
                binding!!.tvFingerprint.visibility = View.INVISIBLE
                binding!!.img2Player.visibility = View.INVISIBLE
                binding!!.tv2Player.visibility = View.INVISIBLE
                binding!!.imgEye.visibility = View.VISIBLE
                binding!!.tvEye.visibility = View.VISIBLE
                AdsManager.loadBannerCollapsibleAds(this,binding!!.bannerHome)
                setLocale(this,getPreLanguage(this))

            }
        }
    }

    //set truth or lying use key event
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    result = 1
                }
                true
            }

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    result = 2
                }
                true
            }
            else -> super.dispatchKeyEvent(event)
        }
    }

    override fun onResume() {
        super.onResume()
        navTab = Common.getGameplay(this)!!
        Log.d("check_tab", navTab)
        bottomNavigationClick()
        changeFragment()
        setLocale(this,getPreLanguage(this))
    }

}