package com.example.liedetector.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication.getFirtOpen
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.example.liedetector.firebase.FirebaseManager
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private val SPLASH_TIME_OUT: Long = 4000 // milliseconds
    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setLocale(this@SplashActivity, getPreLanguage(this))

       binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Common.setLetStart2Player(this, false)

        FirebaseManager.initialize(this)
        FirebaseManager.logEvent("screen_splash")
        FirebaseManager.clickButon()

        if(getFirtOpen(this)){
            getFirebase()
        }





        handler.postDelayed({
            // Tạo Intent để chuyển sang màn hình mới
            val intent = Intent(this@SplashActivity, Splash2Activity::class.java)
            startActivity(intent)

            // Đóng Activity hiện tại để không thể quay lại màn hình Splash bằng nút Back
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun getFirebase(){
        val remoteConfig = FirebaseManager.getFirebaseRemoteConfig()
        if (remoteConfig != null) {
            remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                        val exampleParam = remoteConfig.getBoolean("active_spam")
                        Log.d("FirebaseRemoteConfig","Get firebase")
                    Log.d("FirebaseRemoteConfig","exampleParam $exampleParam")
                        // nếu exampleParam null thì configValue là false
                      val configValue = exampleParam ?: false
                    Log.d("FirebaseRemoteConfig","configValue $configValue")
                    Common.setIsShowAds(this,configValue)

                } else {
                    // Xử lý lỗi khi không thể tải cấu hình từ xa
                    Log.d("FirebaseRemoteConfig","Xử lý lỗi khi không thể tải cấu hình từ xa")
                    val exception = task.exception
                    Log.e("FirebaseRemoteConfig", "Error fetching config", exception)
                }
            }
        } else {
            // Xử lý khi FirebaseRemoteConfig không khả dụng
            Log.d("firebase","Xử lý khi FirebaseRemoteConfig không khả dụng")
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            // Tạo Intent để chuyển sang màn hình mới
            val intent = Intent(this@SplashActivity, Splash2Activity::class.java)
            startActivity(intent)

            // Đóng Activity hiện tại để không thể quay lại màn hình Splash bằng nút Back
            finish()
        }, SPLASH_TIME_OUT)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}

