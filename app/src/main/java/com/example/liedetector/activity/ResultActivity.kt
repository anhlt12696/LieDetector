package com.example.liedetector.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.liedetector.adapter.RowAdapter
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.ads.InAppReview
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.example.liedetector.model.RowItem
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ResultActivity : BaseActivity() {

    private lateinit var binding: ActivityResultBinding
    private val handler = Handler()
    private val handler1 = Handler(Looper.getMainLooper())
    private var isLightOn = false

    private val handler2 = Handler(Looper.getMainLooper())
    private var result: Int = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var rowAdapter: RowAdapter

    /* private val textList = listOf(
         RowItem(resources.getString(R.string.truth)),
         RowItem(resources.getString(R.string.lie)),
         RowItem(resources.getString(R.string.not_tell)),

     )*/
    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayer_true: MediaPlayer? = null
    private var mediaPlayer_false: MediaPlayer? = null
    private val handlerSound = Handler(Looper.getMainLooper())
    private var isMusicPlaying = false
    private lateinit var soundJob: Job



    //bg nhap nhay
    private lateinit var handler3: Handler
    private var currentIndex = 0
    private val drawables =
        intArrayOf(R.drawable.bg_truth, R.drawable.bg_lie, R.drawable.bg_not_tell)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdsManager.loadBannerCollapsibleAds(this,binding.flNative)
        //sound
        mediaPlayer = MediaPlayer.create(this, R.raw.result)
        mediaPlayer_true = MediaPlayer.create(this, R.raw.dung)
        mediaPlayer_false = MediaPlayer.create(this, R.raw.sai)

        result = intent.getIntExtra("result", 0)
        Log.d("result_from_home", result.toString())


//        CoroutineScope(Dispatchers.Default).launch {
//            playSoundResult()
//        }


        soundJob = CoroutineScope(Dispatchers.Main).launch {
            playSoundForDuration(5000) // Phát nhạc trong 5 giây
        }

        startFlashingEffect()
        val textList = listOf(
            RowItem(resources.getString(R.string.truth)),
            RowItem(resources.getString(R.string.lie)),
            RowItem(resources.getString(R.string.not_tell)),
            RowItem(resources.getString(R.string.only_god_knows)),
            RowItem(resources.getString(R.string.try_again)),
            RowItem(resources.getString(R.string.not_found)),
        )

        //text run vertical
        rowAdapter = RowAdapter(textList)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = rowAdapter
        startAutoScroll()

        //bg nhap nhay
        binding.cvRcv.background = ContextCompat.getDrawable(this, drawables[0])

        binding.btnRescan.setOnClickListener {
            if (isMusicPlaying) {
                stopSoundtest()
                stopSound()
            }
            if (!Common.getInAppReview(this)) {
                InAppReview.dialogRateApp(layoutInflater,this)
            } else {
                backGame()
            }
        }

//     //    CoroutineScope cho việc quản lý coroutine, Dispatchers.Main để chạy trong luồng chính (UI thread)
//        val scope = CoroutineScope(Dispatchers.Main)
//
//// Khởi tạo coroutine
//        scope.launch {
//            // Đợi 6 giây
//            delay(6000)
//
//            // Sau đó gán LinearLayoutManager
//            binding.recyclerView.layoutManager = object : LinearLayoutManager(this@ResultActivity) {
//                override fun canScrollVertically(): Boolean = false
//            }
//
//        }



    }

    private fun backGame() {
        val game = intent.getStringExtra("game")
        when (game) {
            "one_player" -> {
                Common.setGamePlay(this, "one_player")
                val intent = Intent(this@ResultActivity, HomeActivity::class.java)
                startActivity(intent)
                Log.d("game_play", "one_player")
            }

            "eye" -> {
                Common.setGamePlay(this, "eye")
                val intent = Intent(this@ResultActivity, HomeActivity::class.java)
                startActivity(intent)
                Log.d("game_play", "eye")
            }
        }
    }

    private fun startFlashingEffect() {
        // Schedule the first toggle after 0 milliseconds (immediately)
        handler1.postDelayed(object : Runnable {
            override fun run() {
                toggleLight()
                // Schedule the next toggle after 500 milliseconds
                handler1.postDelayed(this, 200)


            }
        }, 0)
        handler1.postDelayed({
            handler1.removeCallbacksAndMessages(null)
        }, 5000)
    }

    private fun toggleLight() {
        // Toggle the background color to create the flashing effect
        if (isLightOn) {
            binding.view.background = ContextCompat.getDrawable(
                this,
                R.drawable.ic_led_truth
            )
            binding.view1.background = ContextCompat.getDrawable(
                this,
                R.drawable.ic_led_not_tell
            )

        } else {
            binding.view.background = ContextCompat.getDrawable(
                this,
                R.drawable.ic_led_not_tell
            )
            binding.view1.background = ContextCompat.getDrawable(
                this,
                R.drawable.ic_led_lie
            )

        }
        currentIndex = (currentIndex + 1) % drawables.size
        binding.cvRcv.background = ContextCompat.getDrawable(
            this,
            drawables[currentIndex]
        )
        // Invert the light state
        isLightOn = !isLightOn
    }

    private fun startAutoScroll() {

        handler2.postDelayed(object : Runnable {
            override fun run() {
                // Scroll to the next item
                val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition =
                    if (currentPosition < rowAdapter.itemCount - 1) currentPosition + 1 else 0
                binding.recyclerView.smoothScrollToPosition(nextPosition)
                // Schedule the next scroll after a delay
                handler2.postDelayed(this, 100)
            }
        }, 100)

        handler2.postDelayed({
            val rd = Random.nextInt(5) + 1
            Log.d("result_", "result rd $rd")
            when (result) {
                1 -> {
                    binding.recyclerView.smoothScrollToPosition(0)

                    binding.view.background =
                        ContextCompat.getDrawable(this, R.drawable.ic_led_truth)
                    binding.view1.background =
                        ContextCompat.getDrawable(this, R.drawable.ic_led_not_tell)
                    binding.imIcon.background = ContextCompat.getDrawable(this, R.drawable.ic_true)
                    binding.llIcon.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_bottom_truth)
                    binding.cvRcv.background = ContextCompat.getDrawable(this, drawables[0])

                        playSound(result)

                    Log.d("result_", "set true")


                }

                2 -> {
                    binding.recyclerView.smoothScrollToPosition(1)
                    binding.view.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_led_not_tell
                    )
                    binding.view1.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_led_lie
                    )
                    binding.imIcon.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_lie
                    )
                    binding.llIcon.background = ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_bottom_lie
                    )

                    binding.cvRcv.background = ContextCompat.getDrawable(
                        this,
                        drawables[1]
                    )

                        playSound(result)
                    Log.d("result_", "set false")
                }

                else -> {

                    binding.recyclerView.smoothScrollToPosition(rd)
                    when (rd) {
                        1 -> {
                            binding.recyclerView.smoothScrollToPosition(0)
                            binding.view.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_truth
                            )
                            binding.view1.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_not_tell
                            )
                            binding.imIcon.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_true
                            )
                            binding.llIcon.background =
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.bg_bottom_truth
                                )

                            binding.cvRcv.background = ContextCompat.getDrawable(
                                this,
                                drawables[0]
                            )

                                playSound(1)
                            Log.d("result_", "true $rd " )



                        }

                        2 -> {
                            binding.recyclerView.smoothScrollToPosition(1)
                            binding.view.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_not_tell
                            )
                            binding.view1.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_lie
                            )
                            binding.imIcon.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_lie
                            )
                            binding.llIcon.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.bg_bottom_lie
                            )

                            binding.cvRcv.background = ContextCompat.getDrawable(
                                this,
                                drawables[1]
                            )

                                playSound(2)
                            Log.d("result_", "false $rd " )


                        }
                        else -> {
                            binding.recyclerView.smoothScrollToPosition(rd)
                            binding.view.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_not_tell
                            )
                            binding.view1.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_led_not_tell
                            )
                            binding.imIcon.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_not_tell
                            )
                            binding.llIcon.background =
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.bg_bottom_not_tell
                                )

                            binding.cvRcv.background = ContextCompat.getDrawable(
                                this,
                                drawables[2]
                            )
                            Log.d("result_", "random $rd " )

                        }
                    }
                }
            }
            handler2.removeCallbacksAndMessages(null)

        }, 5000)

    }

    fun showRatingAndFeedbackDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message of the dialog
        alertDialogBuilder.setTitle("Rate Our App")
        alertDialogBuilder.setMessage("If you enjoy using our app, please take a moment to rate it. Your feedback is important to us!")

        // Set the positive button and its click listener
        alertDialogBuilder.setPositiveButton("Rate Now") { dialog, which ->
            // Redirect the user to the app's page on the Play Store for rating
            redirectToPlayStore(context)
            Common.setInAppReview(this, true)
        }

        // Set the negative button and its click listener
        alertDialogBuilder.setNegativeButton("Later") { dialog, which ->
            // Dismiss the dialog, or you can perform other actions
            dialog.dismiss()
            Common.setInAppReview(this, true)
        }

        // Set the neutral button and its click listener
        alertDialogBuilder.setNeutralButton("Submit Feedback") { dialog, which ->
            // Redirect the user to the app's page on the Play Store for submitting feedback
            redirectToPlayStoreForFeedback(context)
            Common.setInAppReview(this, true)
        }
        alertDialogBuilder.setOnDismissListener {
            backGame()
        }

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.setCancelable(false)
    }


    private fun redirectToPlayStore(context: Context) {
        // Redirect the user to the app's page on the Play Store for rating
        try {
            val appPackageName = context.packageName
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            )
        } catch (e: ActivityNotFoundException) {
            // If the Play Store app is not installed, open the app page in a web browser
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                )
            )
        }
    }

    private fun redirectToPlayStoreForFeedback(context: Context) {
        // Redirect the user to the app's page on the Play Store for submitting feedback
        try {
            val appPackageName = context.packageName
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName&showAllReviews=true")
                )
            )
        } catch (e: ActivityNotFoundException) {
            // If the Play Store app is not installed, open the app page in a web browser
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}&showAllReviews=true")
                )
            )
        }
    }

    private fun playSound(result: Int) {
        if (Common.getSound(this)){
            try {
                when(result){
                    1 -> mediaPlayer_true?.start() // Bắt đầu phát âm thanh}
                    2 -> mediaPlayer_false?.start() // Bắt đầu phát âm thanh
                }
                handlerSound.postDelayed({
                    stopSound()
                }, 3000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        }

    private fun stopSound() {
        //  mediaPlayer?.pause() // Tạm dừng âm thanh
        try {
                mediaPlayer_true!!.pause() // Tạm dừng nhạc
                mediaPlayer_false!!.pause() // Tạm dừng nhạc
                mediaPlayer_true!!.release()
                mediaPlayer_false!!.release()
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
            Log.d("sound_error", e.printStackTrace().toString())
        }

    }

//    private fun playSoundResult() {
//        if(Common.getSound(this)){
//            try {
//                mediaPlayer?.start() // Bắt đầu phát âm thanh
//                isMusicPlaying = true
//                handlerSound.postDelayed({
//                    stopSoundResult()
//                }, 5000)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//    }
//
//    private fun stopSoundResult() {
//        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
//            try {
//                mediaPlayer!!.stop()
//                mediaPlayer!!.release() // Quay lại đầu nhạc
//                Log.d("play_sound","stop in funtion")
//            } catch (e: IllegalStateException) {
//                e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
//                Log.d("sound_error", e.printStackTrace().toString())
//            }
//        }
//        isMusicPlaying = false
//    }

    private suspend fun playSoundForDuration(durationMillis: Long) {
        if(Common.getSound(this)) {
            try {
                mediaPlayer!!.start()
                isMusicPlaying = true
                delay(durationMillis)
                if (isMusicPlaying) {
                    stopSoundtest()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopSoundtest() {
        try {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            isMusicPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isMusicPlaying) {
            stopSoundtest()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler2.removeCallbacksAndMessages(null)
        handler1.removeCallbacksAndMessages(null)
        handlerSound.removeCallbacksAndMessages(this)
        mediaPlayer!!.release()
        mediaPlayer_true!!.release()
        mediaPlayer_false!!.release()
        soundJob.cancel()
       // stopSound()
        Log.d("play_sound","stop in destroy")

    }
}

