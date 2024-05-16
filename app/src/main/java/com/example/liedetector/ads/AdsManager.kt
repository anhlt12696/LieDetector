package com.example.liedetector.ads

import android.app.Activity
import android.content.Context.WINDOW_SERVICE
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication.getSystemService
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoadCallback
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.OnPaidEventListener

object AdsManager {
    var AdsBanner_Collapsible = "ca-app-pub-8279085421229677/6911066628"
    var AdsBanner_test = "ca-app-pub-3940256099942544/2014213617"
    private lateinit var adView: AdView

    fun initAds(activity: Activity){
        MobileAds.initialize(activity)
    }
    fun loadBannerCollapsibleAds(activity: Activity, frameLayout: FrameLayout) {
        adView = AdView(activity)
        adView.adUnitId = AdsBanner_Collapsible

        val adSize = getAdSize(frameLayout,activity)
        adView.setAdSize(adSize)


        val adRequest:AdRequest
        if (Common.getIsShowAds(activity)) {
            Log.d("LOG_ADS","BANNER COLLAPSIBLE")
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()

        } else {
            Log.d("LOG_ADS","BANNER")
            adRequest = AdRequest.Builder().build()
        }
        frameLayout.addView(adView)
        adView.onPaidEventListener = OnPaidEventListener { adValue ->
            // Code to handle the paid event
            val loadedAdapterResponseInfo = adView.responseInfo!!.loadedAdapterResponseInfo
            val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
            adRevenue.revenue = adValue.valueMicros / 1000000.0
            adRevenue.adRevenueNetwork = loadedAdapterResponseInfo!!.adSourceName
            Adjust.trackAdRevenue(adRevenue)
        }
        adView.adListener = object :AdListener(){
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("LOG_ADS", "onAdFailedToLoad $adError")
                adView.loadAd(adRequest)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("LOG_ADS","onAdLoaded")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("LOG_ADS","onAdOpened")
            }
        }
        adView.loadAd(adRequest)

    }

    fun getAdSize(view: View, activity: Activity): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = view.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }


}