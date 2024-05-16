package com.example.liedetector.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R

object FirebaseManager {

    public var firebaseAnalytics: FirebaseAnalytics? = null
    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null


    fun initialize(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults) // Set default values from XML file
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600 // Fetch minimum every hour, adjust according to your needs
            }
            setConfigSettingsAsync(configSettings)
        }
    }

    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics?.logEvent(eventName, params)
    }

    fun clickButon(){
        val bundle = Bundle()
        bundle.putString("state_button", "ON")
        firebaseAnalytics!!.logEvent("click_button_on_off", bundle)
        bundle.clear()
    }

    fun fetchRemoteConfig() {
        firebaseRemoteConfig?.fetchAndActivate()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Remote Config fetch succeeded, activate the fetched data
                } else {
                    // Remote Config fetch failed
                }
            }
    }

    fun getFirebaseRemoteConfig(): FirebaseRemoteConfig? {
        return firebaseRemoteConfig
    }



}