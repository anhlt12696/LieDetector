package com.example.liedetector.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liedetector.adapter.LanguageAdapter
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.ads.AppOpenAds
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.MyApplication.getPosition
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.getPreLanguageName
import com.example.liedetector.base.MyApplication.getPreLanguageflag
import com.example.liedetector.base.MyApplication.setFirtOpen
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.base.MyApplication.setPosition
import com.example.liedetector.base.MyApplication.setPreLanguageName
import com.example.liedetector.base.MyApplication.setPreLanguageflag
import com.example.liedetector.model.Language
import java.util.ArrayList
import com.example.liedetector.firebase.FirebaseManager
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityLanguageBinding
import java.util.Locale

class LanguageActivity : BaseActivity() {
    lateinit var binding: ActivityLanguageBinding
    var adapterLanguage: LanguageAdapter? = null
    var listTitle: ArrayList<Language> = ArrayList()
    var listImage: ArrayList<Int> = ArrayList()
    var language: String? = null
    var language_name: String = ""
    var flag = 0
    var pos = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Language__","create")
        FirebaseManager.logEvent("screen_language")
        FirebaseManager.clickButon()
      //  AppLovinManager.showAd()

//        AppLovinManager.createNativeAd(this,binding.flNative)
//        Common.coldStartCompleted = true


        listTitle.add(Language(resources.getString(R.string.english), "en"))
        listTitle.add(Language(resources.getString(R.string.hindi), "hi"))
        listTitle.add(Language(resources.getString(R.string.spanish), "es"))
        listTitle.add(Language(resources.getString(R.string.friench), "fr"))
        listTitle.add(Language(resources.getString(R.string.arabic), "ar"))
        listTitle.add(Language(resources.getString(R.string.bengali), "bn"))
        listTitle.add(Language(resources.getString(R.string.russian), "ru"))
        listTitle.add(Language(resources.getString(R.string.portuguese), "pt"))
        listTitle.add(Language(resources.getString(R.string.indonesian), "in"))
        listTitle.add(Language(resources.getString(R.string.german), "de"))
        listTitle.add(Language(resources.getString(R.string.italian), "it"))
        listTitle.add(Language(resources.getString(R.string.korean), "ko"))
        listImage.add(R.drawable.ic_eng_2x)
        listImage.add(R.drawable.ic_hindi)
        listImage.add(R.drawable.ic_span)
        listImage.add(R.drawable.ic_fr)
        listImage.add(R.drawable.ic_arabic)
        listImage.add(R.drawable.ic_bengali)
        listImage.add(R.drawable.ic_russian)
        listImage.add(R.drawable.ic_portuga)
        listImage.add(R.drawable.ic_indo)
        listImage.add(R.drawable.ic_german)
        listImage.add(R.drawable.ic_italy)
        listImage.add(R.drawable.ic_kor)

        language = this.getPreLanguage(this)
        Log.d("Language__","language in onCreate " + language.toString())
        language_name = getPreLanguageName(this).toString()
        flag = this.getPreLanguageflag(this)
        pos = this.getPosition(this)

        AdsManager.loadBannerCollapsibleAds(this,binding.flNative)

        adapterLanguage = LanguageAdapter(this, object : LanguageAdapter.onClickLanguage {
            override fun onClicked(position: Int, name: String, img: Int, language_name: String) {
                adapterLanguage?.updateData(position)
                pos = position
                language = name
                flag = img
                this@LanguageActivity.language_name = language_name
            }
        })
//


        binding.rcvLanguage.layoutManager = LinearLayoutManager(this)
        adapterLanguage?.setData(listTitle, listImage)
        binding.rcvLanguage.adapter = adapterLanguage
        adapterLanguage?.updateData(pos)

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.done.setOnClickListener {
            setLocale(this@LanguageActivity, language)
            Log.d("Language__","language when click button " + language.toString())
            setPreLanguageflag(this@LanguageActivity, flag)
            setPosition(this@LanguageActivity, pos)
            //ktra
            setFirtOpen(this, false)
            setPreLanguageName(this, language_name)

            if (intent.getBooleanExtra("home_language", false)) {
                val i = Intent(this@LanguageActivity, HomeActivity::class.java)
                binding.backArrow.visibility = View.VISIBLE
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            } else {
                val launchIntro: Intent = Intent(this@LanguageActivity, TipsActivity::class.java)
                startActivity(launchIntro)
            }
        }
        // ẩn hiện icon back
        if (intent.getBooleanExtra("home_language", false)){
            binding.backArrow.visibility = View.VISIBLE
        }else{
            binding.backArrow.visibility = View.INVISIBLE
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("setting", false)) {
            binding.backArrow.visibility = View.VISIBLE
        }
        Log.d("Language__","onResume")
//        language = this.getPreLanguage(this)
//        Log.d("Language__", "language on onResume " + language.toString())
//        language_name = getPreLanguageName(this).toString()
//        flag = this.getPreLanguageflag(this)
//        pos = this.getPosition(this)
//        setLocale(this@LanguageActivity, language)
//        adapterLanguage = LanguageAdapter(this, object : LanguageAdapter.onClickLanguage {
//            override fun onClicked(position: Int, name: String, img: Int, language_name: String) {
//                adapterLanguage?.updateData(position)
//                pos = position
//                language = name
//                flag = img
//                this@LanguageActivity.language_name = language_name
//            }
//        })
////
//        binding.rcvLanguage.layoutManager = LinearLayoutManager(this)
//        adapterLanguage?.setData(listTitle, listImage)
//        binding.rcvLanguage.adapter = adapterLanguage
//        adapterLanguage?.updateData(pos)
    }

    override fun onStart() {
        super.onStart()
        Log.d("Language__","start")
        Log.d("Language__", "language on onStart " + language.toString())

    }

    override fun onPause() {
        super.onPause()
        Log.d("Language__","pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Language__","stop")
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Language__","onDestroy")
    }
}