package com.example.liedetector.base

import android.app.Application
import android.content.Context

object Common {
    var coldStartCompleted = false
    fun setPerMission(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_PERMISSION", open).apply()
    }

    fun getPerMission(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_PERMISSION", false)
    }

    fun setGamePlay(context: Context,gameplay : String?){
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putString("KEY_GAMEPLAY", gameplay).apply()
    }

    fun getGameplay(mContext: Context) :String?{
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getString("KEY_GAMEPLAY", "one_player")
    }

    fun setVibration(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_VIBRATION", open).apply()
    }

    fun getVibration(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_VIBRATION", true)
    }

    fun setSound(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_SOUND", open).apply()
    }

    fun getSound(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_SOUND", true)
    }

    fun setLetStart2Player(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_LET_START", open).apply()
    }

    fun getLetStart2Player(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_LET_START", false)
    }

    fun getInAppReview(mContext: Context): Boolean {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getBoolean("KEY_IN_APP_REVIEW", false)
    }
    fun setInAppReview(context: Context, open: Boolean) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putBoolean("KEY_IN_APP_REVIEW", open).apply()
    }

    fun getIsShowAds(mContext: Context): Boolean {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Application.MODE_MULTI_PROCESS
        )
        return preferences.getBoolean("KEY_SHOW_ADS", false)
    }
    fun setIsShowAds(context: Context, open: Boolean) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Application.MODE_MULTI_PROCESS
        )
        preferences.edit().putBoolean("KEY_SHOW_ADS", open).apply()
    }
}