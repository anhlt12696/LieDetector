package com.example.liedetector.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.MyApplication.setFirtOpen
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivitySplash2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel


class Splash2Activity : BaseActivity() {
    private lateinit var binding: ActivitySplash2Binding
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setFirtOpen(this,false)
        handler.postDelayed({
            // Tạo Intent để chuyển sang màn hình mới
            val intent = Intent(this@Splash2Activity, LanguageActivity::class.java)
            startActivity(intent)

            // Đóng Activity hiện tại để không thể quay lại màn hình Splash bằng nút Back
            finish()
        }, 4000)



    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({
            // Tạo Intent để chuyển sang màn hình mới
            val intent = Intent(this@Splash2Activity, LanguageActivity::class.java)
            startActivity(intent)

            // Đóng Activity hiện tại để không thể quay lại màn hình Splash bằng nút Back
            finish()
        }, 4000)

    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onStop() {
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        // Hủy lên lịch khi activity bị hủy
        handler.removeCallbacksAndMessages(null)
    }
    }

