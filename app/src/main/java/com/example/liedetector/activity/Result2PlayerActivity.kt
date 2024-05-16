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
import com.example.liedetector.adapter.Row2PlayerAdapter
import com.example.liedetector.ads.AdsManager
import com.example.liedetector.ads.InAppReview
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.example.liedetector.model.RowItem
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityResult2PlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class Result2PlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityResult2PlayerBinding
    private lateinit var rowAdapter: Row2PlayerAdapter
    private val handler2 = Handler(Looper.getMainLooper())
    private val handler1 = Handler(Looper.getMainLooper())
    private var result: Int = 0

    private val handler3 = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private val drawables1 =
        intArrayOf(R.drawable.bg_result_2_player_truth_1, R.drawable.bg_result_2_player_lie_1)
    private val drawables2 =
        intArrayOf(R.drawable.bg_result_2_player_truth_2, R.drawable.bg_result_2_player_lie_2)

    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayer_true: MediaPlayer? = null
    private var mediaPlayer_false: MediaPlayer? = null
    private val handlerSound = Handler(Looper.getMainLooper())
    private var isMusicPlaying = false
    private lateinit var soundJob: Job

    private var result_player_1 = false
    private var result_player_2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResult2PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        result = intent.getIntExtra("result", 0)
        Log.d("result", result.toString())
        AdsManager.loadBannerCollapsibleAds(this, binding.flNative)
        //sound
        mediaPlayer = MediaPlayer.create(this, R.raw.result)
        mediaPlayer_true = MediaPlayer.create(this, R.raw.dung)
        mediaPlayer_false = MediaPlayer.create(this, R.raw.sai)

        val textList = listOf(
            RowItem(resources.getString(R.string.truth)),
            RowItem(resources.getString(R.string.lie)),
            RowItem(resources.getString(R.string.not_tell)),
            RowItem(resources.getString(R.string.only_god_knows)),
            RowItem(resources.getString(R.string.try_again)),
            RowItem(resources.getString(R.string.not_found)),
        )

        //text run vertical
        rowAdapter = Row2PlayerAdapter(textList)

        binding.rcvPlayer1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvPlayer1.adapter = rowAdapter

        binding.rcvPlayer2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvPlayer2.adapter = rowAdapter
        startAutoScroll()
        startAutoScroll2()
        soundJob = CoroutineScope(Dispatchers.Main).launch {
            playSoundForDuration(5000) // Phát nhạc trong 5 giây
        }

        val delayedRunnable = Runnable {
            playSound()
        }
        handlerSound.postDelayed(delayedRunnable, 5000)

        binding.btnRescan.setOnClickListener {
            Common.setGamePlay(this, "two_player")
            if (isMusicPlaying) {
                stopSoundtest()
                stopSound()
                handlerSound.removeCallbacks(delayedRunnable)
            }
            if (!Common.getInAppReview(this)) {
                // showRatingAndFeedbackDialog(this)
                InAppReview.dialogRateApp(layoutInflater, this)
            } else {
                val intent = Intent(this@Result2PlayerActivity, HomeActivity::class.java)
                startActivity(intent)
            }

        }
        //bg nhap nhay
        binding.cvRcvP1.background = ContextCompat.getDrawable(
            this,
            drawables1[0]
        )
        binding.cvRcvP2.background = ContextCompat.getDrawable(
            this,
            drawables2[0]
        )
        startFlashingEffect()

    }

    private fun startAutoScroll() {

        handler2.postDelayed(object : Runnable {
            override fun run() {
                // Scroll to the next item
                val layoutManager = binding.rcvPlayer1.layoutManager as LinearLayoutManager
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition =
                    if (currentPosition < rowAdapter.itemCount - 1) currentPosition + 1 else 0
                binding.rcvPlayer1.smoothScrollToPosition(nextPosition)
                // Schedule the next scroll after a delay
                handler2.postDelayed(this, 300)


            }
        }, 300)

        handler2.postDelayed({
            when (result) {
                1 -> {
                    binding.rcvPlayer1.smoothScrollToPosition(0)
                    binding.cvRcvP1.background = ContextCompat.getDrawable(
                        this,
                        drawables1[0]
                    )
                    result_player_1 = true

                    Log.d("result_2_play", "player 1 $result")

                }

                2 -> {
                    binding.rcvPlayer1.smoothScrollToPosition(1)
                    binding.cvRcvP1.background = ContextCompat.getDrawable(
                        this,
                        drawables1[1]
                    )
                    result_player_1 = false
                    Log.d("result_2_play", "player 1 $result")
                }

                else -> {
                    val rd = Random.nextInt(2)
                    binding.rcvPlayer1.smoothScrollToPosition(rd)
                    if (rd == 0) {
                        binding.rcvPlayer1.smoothScrollToPosition(0)
                        binding.cvRcvP1.background = ContextCompat.getDrawable(
                            this,
                            drawables1[0]
                        )
                        result_player_1 = true

                        Log.d("result_2_play", "player 1 rd $rd")

                    } else if (rd == 1) {
                        binding.rcvPlayer1.smoothScrollToPosition(1)
                        binding.cvRcvP1.background = ContextCompat.getDrawable(
                            this,
                            drawables1[1]
                        )

                        result_player_1 = false
                        Log.d("result_2_play", "player 1 rd  $rd")
                    }
                }
            }
            handler2.removeCallbacksAndMessages(null)
            binding.rcvPlayer1.isNestedScrollingEnabled = false
            binding.rcvPlayer1.isFocusable = false

        }, 5000)

    }

    private fun startAutoScroll2() {

        handler1.postDelayed(object : Runnable {
            override fun run() {
                // Scroll to the next item


                val layoutManager2 = binding.rcvPlayer2.layoutManager as LinearLayoutManager
                val currentPosition2 = layoutManager2.findFirstVisibleItemPosition()
                val nextPosition2 =
                    if (currentPosition2 < rowAdapter.itemCount - 1) currentPosition2 + 1 else 0
                binding.rcvPlayer2.smoothScrollToPosition(nextPosition2)
                // Schedule the next scroll after a delay
                handler2.postDelayed(this, 200)

            }
        }, 200)

        handler1.postDelayed({

            val rd = Random.nextInt(3)
            binding.rcvPlayer2.smoothScrollToPosition(rd)
            when (rd) {
                0 -> {
                    binding.rcvPlayer2.smoothScrollToPosition(0)
                    binding.cvRcvP2.background = ContextCompat.getDrawable(
                        this,
                        drawables2[0]
                    )
                    result_player_2 = true

                    Log.d("result_2_play", "player 2 0")

                }

                1 -> {
                    binding.rcvPlayer2.smoothScrollToPosition(1)
                    binding.cvRcvP2.background = ContextCompat.getDrawable(
                        this,
                        drawables2[1]
                    )
                    result_player_2 = false

                    Log.d("result_2_play", "player 2 $rd")

                }

                else -> {
                    binding.rcvPlayer2.smoothScrollToPosition(1)
                    binding.cvRcvP2.background = ContextCompat.getDrawable(
                        this,
                        drawables2[1]
                    )
                    result_player_2 = false

                    Log.d("result_2_play", "player 2 $rd")

                }
            }
            handler2.removeCallbacksAndMessages(null)


        }, 5000)

    }

    //bg nhap nhay
    private fun startFlashingEffect() {
        // Schedule the first toggle after 0 milliseconds (immediately)
        handler3.postDelayed(object : Runnable {
            override fun run() {
                toggleBg()
                // Schedule the next toggle after 500 milliseconds
                handler3.postDelayed(this, 200)
            }
        }, 0)
        handler3.postDelayed({
            handler3.removeCallbacksAndMessages(null)
        }, 5000)
    }

    private fun toggleBg() {
        // Toggle the background color to create the flashing effect

        currentIndex = (currentIndex + 1) % drawables1.size
        binding.cvRcvP1.background = ContextCompat.getDrawable(
            this,
            drawables1[currentIndex]
        )
        binding.cvRcvP2.background = ContextCompat.getDrawable(
            this,
            drawables2[currentIndex]
        )

        // Invert the light state

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
            val intent = Intent(this@Result2PlayerActivity, HomeActivity::class.java)
            startActivity(intent)
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

    private fun playSound() {
        if (Common.getSound(this)) {
            try {
                if (result_player_1 && result_player_2) {
                    mediaPlayer_true?.start() // Bắt đầu phát âm thanh}
                } else {
                    mediaPlayer_false?.start()
                }
                isMusicPlaying = true

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopSound() {
        //  mediaPlayer?.pause() // Tạm dừng âm thanh
        try {
            if (mediaPlayer_true!!.isPlaying) {
                mediaPlayer_true!!.pause() // Tạm dừng nhạc
                mediaPlayer_true!!.seekTo(0) // Quay lại đầu nhạc
            }
            if (mediaPlayer_false!!.isPlaying) {
                mediaPlayer_false!!.pause() // Tạm dừng nhạc
                mediaPlayer_false!!.seekTo(0) // Quay lại đầu nhạc
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
            Log.d("sound_error", e.printStackTrace().toString())
        }
        isMusicPlaying = false
    }

    private suspend fun playSoundForDuration(durationMillis: Long) {
        if (Common.getSound(this)) {
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

    }
}