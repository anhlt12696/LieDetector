package com.example.liedetector.fragment

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.liedetector.activity.HomeActivity
import com.example.liedetector.activity.Result2PlayerActivity
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.FragmentTwoPlayerBinding
import java.util.ArrayList
import kotlin.random.Random


class TwoPlayerFragment : Fragment() {
    private lateinit var binding: FragmentTwoPlayerBinding
    private lateinit var vibrator: Vibrator
    private var isButton1Held = false
    private var isButton2Held = false

    private val handler = Handler()
    private val handler1 = Handler(Looper.getMainLooper())
    private var result1: Int? = null
    private var isLightOn = false
    private var delayedTask: Runnable? = null
    private var led: Runnable? = null
    private var listQuestion: ArrayList<String> = ArrayList()
    private var rd_question = 1

    private var mediaPlayer: MediaPlayer? = null
    private val handlerSound = Handler(Looper.getMainLooper())

    private lateinit var animationView: LottieAnimationView

    private val handler3 = Handler()
    private var counter = 5

    private val textUpdateRunnable = object : Runnable {

        override fun run() {
            // Update the text with the current value of counter
            val text = "$counter.."
            binding.tvQuestion.text = text

            // Increment the counter
            counter--

            // If the counter exceeds 5, reset it to 1
            if (counter == -1 ) {
                binding.tvQuestion.text = getText(R.string.analyzing)
                return
            }

            // Re-post the Runnable to run after a delay
            handler3.postDelayed(this, 900) // Delay in milliseconds (e.g., 1000ms = 1 second)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTwoPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        binding.btnPlayer1.playAnimation()
        binding.btnPlayer2.playAnimation()


        binding.btnPlayer1.setOnTouchListener { _, event ->
            handleTouch(event, binding.btnPlayer1)
        }

        binding.btnPlayer2.setOnTouchListener { v, event ->
            handleTouch(event, binding.btnPlayer2)
        }

        delayedTask = Runnable {
            showDialog()
        }

        led = Runnable {
            startFlashingEffect()
        }

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.file_mp3)

        //question
        listQuestion.add(resources.getString(R.string.question))
        listQuestion.add(resources.getString(R.string.question_2))
        listQuestion.add(resources.getString(R.string.question_3))
        listQuestion.add(resources.getString(R.string.question_4))
        listQuestion.add(resources.getString(R.string.question_5))
        listQuestion.add(resources.getString(R.string.question_6))
        listQuestion.add(resources.getString(R.string.question_7))
        listQuestion.add(resources.getString(R.string.question_8))
        listQuestion.add(resources.getString(R.string.question_9))
        listQuestion.add(resources.getString(R.string.question_10))


        rd_question = Random.nextInt(9)
        binding.tvQuestion.text = listQuestion[rd_question]



//        // Tạo ObjectAnimator để di chuyển button theo hướng dọc
//        val animator = ObjectAnimator.ofFloat(binding.btnLetStart, "translationX",  0f, -30f, 30f,-30f,0f)
//        // Thiết lập thời gian diễn ra hiệu ứng
//        animator.duration = 1500
//        // Thiết lập lặp lại vô hạn
//        animator.repeatCount = ObjectAnimator.INFINITE
//        animator.repeatMode = ObjectAnimator.REVERSE
//        animator.start()
    }

    private fun handleTouch(event: MotionEvent, button: LottieAnimationView): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Button is held down
                when (button.id) {
                    R.id.btn_player_1 -> isButton1Held = true
                    R.id.btn_player_2 -> isButton2Held = true

                }
                binding.llContent.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_2_player_text_content_red
                )
                binding.tvContent.setTextColor(resources.getColor(R.color.text_content_color_red_1))

                checkIfBothButtonsHeld()
                return true
            }

            MotionEvent.ACTION_UP -> {
                // Button is released
                when (button.id) {
                    R.id.btn_player_1 -> isButton1Held = false
                    R.id.btn_player_2 -> isButton2Held = false
                }
                binding.llContent.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_2_player_text_content
                )
                binding.tvContent.setTextColor(resources.getColor(R.color.text_content_color))
                checkIfBothButtonsHeld()
                return true
            }
        }
        return false
    }

    private fun checkIfBothButtonsHeld() {
        if (isButton1Held && isButton2Held) {
            if (Common.getVibration(requireContext())) {
                startVibration()
            }
            if (Common.getSound(requireContext())) {
                playSound()
            }
            binding.llContent.visibility = View.GONE
            binding.imgLoading.visibility = View.VISIBLE

            binding.btnPlayer1.visibility = View.INVISIBLE
            binding.btnPlayer1Scan.visibility = View.VISIBLE
            binding.btnPlayer2.visibility = View.INVISIBLE
            binding.btnPlayer2Scan.visibility = View.VISIBLE

            counter = 5
            //start anim
            animationView = binding.animLoading
            animationView.progress = 0f
            animationView.resumeAnimation()
            handler3.removeCallbacks(textUpdateRunnable)
            handler3.post(textUpdateRunnable)
            handler.postDelayed(led!!, 0)
            handler.postDelayed({
                if (isButton1Held && isButton2Held) {
                    handler.postDelayed(delayedTask!!, 100)

                } else {
                    handler.removeCallbacks(delayedTask!!)
                    handler.removeCallbacks(led!!)
                    handler3.removeCallbacks(textUpdateRunnable)
                    handler1.removeCallbacksAndMessages(null)
                    handler.removeCallbacksAndMessages(null)
                    binding.llContent.visibility = View.VISIBLE
                    binding.imgLoading.visibility = View.GONE
                }
            }, 5000)


        } else {
            stopVibration()
            stopSound()
            handler.removeCallbacks(delayedTask!!)
            handler.removeCallbacks(led!!)
            handler.removeCallbacksAndMessages(null)
            handler1.removeCallbacksAndMessages(null)
            handler3.removeCallbacks(textUpdateRunnable)

            binding.llContent.visibility = View.VISIBLE
            binding.imgLoading.visibility = View.GONE

            binding.btnPlayer1.visibility = View.VISIBLE
            binding.btnPlayer1Scan.visibility = View.INVISIBLE
            binding.btnPlayer2.visibility = View.VISIBLE
            binding.btnPlayer2Scan.visibility = View.INVISIBLE
            binding.tvQuestion.text = listQuestion[rd_question]

        }
    }

    private fun startVibration() {
        // Check if the device supports vibration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(5000)
        }

    }

    private fun stopVibration() {
        vibrator.cancel()
    }

    private fun showDialog() {

        val customLayout = layoutInflater.inflate(R.layout.success_dialog, null)
        val customButton = customLayout.findViewById<Button>(R.id.btn_ok)
        // Create the AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(customLayout)
        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.background_transparent)
        alertDialog.show()
        alertDialog.setCancelable(false)
        customButton.setOnClickListener {
            alertDialog.dismiss()
            result1 = (activity as? HomeActivity)?.result
            getResult()
            val intent = Intent(requireActivity(), Result2PlayerActivity::class.java)
            intent.putExtra("result", result1)
            intent.putExtra("game", "two_player")
            startActivity(intent)
            result1 = null

        }
    }

    private fun getResult(): Int? {
        if (result1 == null) {
            result1 = Random.nextInt(4)
        }
        return result1
    }

    private fun startFlashingEffect() {
        // Schedule the first toggle after 0 milliseconds (immediately)
        handler1.postDelayed(object : Runnable {
            override fun run() {
                toggleLight()
                // Schedule the next toggle after 500 milliseconds
                handler1.postDelayed(this, 100)
            }
        }, 0)

        handler1.postDelayed({
            handler1.removeCallbacksAndMessages(null)
        }, 5000)
    }


    private fun toggleLight() {
        // Toggle the background color to create the flashing effect
        if (isLightOn) {
            binding.imgLed1.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_led_2_player_true
            )
            binding.imgLed2.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_led_2_player
            )
        } else {
            binding.imgLed1.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_led_2_player
            )
            binding.imgLed2.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_led_2_player_lie
            )
        }

        // Invert the light state
        isLightOn = !isLightOn
    }

    private fun playSound() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.file_mp3)
        mediaPlayer?.start() // Bắt đầu phát âm thanh
        Log.d("Sound_check","play sound")
    }

    private fun stopSound() {
        //  mediaPlayer?.pause() // Tạm dừng âm thanh
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
            Log.d("sound_error", e.printStackTrace().toString())
        }
    }
    override fun onPause() {
        super.onPause()
        stopVibration()
        stopSound()
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(delayedTask!!)
        handler.removeCallbacks(led!!)
        handler1.removeCallbacksAndMessages(null)
        handler3.removeCallbacks(textUpdateRunnable)
        mediaPlayer?.release()
    }


}