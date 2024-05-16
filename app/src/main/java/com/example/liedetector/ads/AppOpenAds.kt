package com.example.liedetector.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.base.MyApplication.setPreLanguage
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date
import java.util.Locale


class AppOpenAds  : Application(),Application.ActivityLifecycleCallbacks,LifecycleObserver{

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var LOG_TAG = "AppOpenAdManager"
    private var AD_UNIT_ID = "ca-app-pub-8279085421229677/2939714790"
    private var AD_UNIT_ID_TEST = "ca-app-pub-3940256099942544/9257395921"
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this@AppOpenAds)
        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()

        setLocale(this, getPreLanguage(this))

        //adjust
        val appToken = "zsnauzp5181s"
        val environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(this, appToken, environment)

        config.setLogLevel(LogLevel.VERBOSE)
        config.setSendInBackground(true);
        Adjust.onCreate(config)

        Log.d("Test_app_error","$this  On create")



//        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
//        adjustAdRevenue.setRevenue(1.00, "USD")
//        adjustAdRevenue.setAdImpressionsCount(10)
//        adjustAdRevenue.setAdRevenueNetwork("network1")
//        adjustAdRevenue.setAdRevenueUnit("unit1")
//        adjustAdRevenue.setAdRevenuePlacement("banner")
//        adjustAdRevenue.addCallbackParameter("key1", "value1")
//        adjustAdRevenue.addPartnerParameter("key2", "value2")
//        Adjust.trackAdRevenue(adjustAdRevenue)


    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(it)
        }
    }
     inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Request an ad. */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {

                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.
                        Log.d(LOG_TAG, "Ad was loaded.")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                      //  var rewardedAd = ad
                        // Set paid event listener
                        appOpenAd!!.onPaidEventListener =
                            OnPaidEventListener { p0 ->
                                Log.d("adjustttttt", "click qc ads banner")
                                val loadedAdapterResponseInfo = appOpenAd!!.responseInfo.loadedAdapterResponseInfo
                                // send ad revenue info to Adjust
                                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                                adRevenue.revenue = p0.valueMicros / 1000000.0
                                adRevenue.adRevenueNetwork = loadedAdapterResponseInfo!!.adSourceName
                                adRevenue.setAdRevenueUnit(appOpenAd!!.adUnitId)
                                Adjust.trackAdRevenue(adRevenue)
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.
                        Log.d(LOG_TAG, loadAdError.message)
                        isLoadingAd = false;
                    }

                })
        }

        /** Shows the ad if one isn't already showing. */
        fun showAdIfAvailable(
            activity: Activity) {
            if(Common.getIsShowAds(activity)){
                if (isShowingAd) {
                    Log.d(LOG_TAG, "The app open ad is already showing.")
                    return
                }

                // If the app open ad is not available yet, invoke the callback then load the ad.
                if (!isAdAvailable()) {
                    Log.d(LOG_TAG, "The app open ad is not ready yet.")

                    loadAd(activity)
                    return
                }

                appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {
                        // Called when full screen content is dismissed.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
                        appOpenAd = null
                        isShowingAd = false


                        loadAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        loadAd(activity)
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d(LOG_TAG, "Ad showed fullscreen content.")
                    }
                }
                isShowingAd = true
                appOpenAd?.show(activity)
            }else{
                Log.d("LOG_ADS_APP OPEN","NOT SHOW")
            }
            // If the app open ad is already showing, do not show the ad again.



        }
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("Test_app_error","$activity  onActivityCreated ")

    }

    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
        Log.d("Test_app_error","$activity  onActivityStarted ")
    }

    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
        setLocale(this, getPreLanguage(this))
        Log.d("Test_app_error","$activity  onActivityResumed ")
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
        setLocale(this, getPreLanguage(this))
        Log.d("Test_app_error","$activity  onActivityPaused ")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d("Test_app_error","$activity  onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d("Test_app_error","$activity  onActivitySaveInstanceState ")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d("Test_app_error","$activity  onActivityDestroyed ")
    }
}

