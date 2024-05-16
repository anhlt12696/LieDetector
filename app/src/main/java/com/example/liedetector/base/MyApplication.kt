package com.example.liedetector.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.firebase.FirebaseManager
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import java.util.Locale
import java.util.*

 object MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
//        AppLovinSdk.getInstance( this ).mediationProvider = "max"
//        AppLovinSdk.getInstance( this ).initializeSdk {
//            // AppLovin SDK is initialized, start loading ads
//        }

        FirebaseManager.initialize(this)
        setLocale(this, getPreLanguage(this))


    }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.setLocale(activity, activity.getPreLanguage(activity))
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        activity.setLocale(activity, activity.getPreLanguage(activity))
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    fun Context?.getPreLanguage(mContext: Context): String? {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getString("KEY_LANGUAGE", "en")
    }

    fun Context?.setPreLanguage(context: Context, language: String?) {
        if (TextUtils.isEmpty(language)) return
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putString("KEY_LANGUAGE", language).apply()
    }

    fun Context?.getPreLanguageName(mContext: Context): String? {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getString("KEY_LANGUAGE_NAME", "English")
    }

    fun Context?.setPreLanguageName(context: Context, language: String?) {
        if (TextUtils.isEmpty(language)) return
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putString("KEY_LANGUAGE_NAME", language).apply()
    }

    fun Context?.getPreLanguageflag(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getInt("KEY_FLAG", R.drawable.ic_english)
    }

    fun Context?.setPreLanguageflag(context: Context, flag: Int) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putInt("KEY_FLAG", flag).apply()
    }



    fun Context?.setPosition(context: Context, open: Int) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putInt("KEY_POSITION", open).apply()
    }

    fun Context?.getPosition(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getInt("KEY_POSITION", 0)
    }

    fun Context?.setLocale(context: Context, lang: String?) {
        val myLocale = Locale(lang)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        context.setPreLanguage(context, lang)
    }

    fun Context?.getFirtOpen(mContext: Context): Boolean {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getBoolean("KEY_OPEN", true)
    }
    fun Context?.setFirtOpen(context: Context, open: Boolean) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putBoolean("KEY_OPEN", open).apply()
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

}