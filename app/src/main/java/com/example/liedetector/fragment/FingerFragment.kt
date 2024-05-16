package com.example.liedetector.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.liedetector.activity.HomeActivity
import com.example.liedetector.activity.ResultActivity
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.example.liedetector.base.MyApplication
import com.example.liedetector.base.MyApplication.getPreLanguage
import com.example.liedetector.base.MyApplication.setLocale
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.FragmentFingerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class FingerFragment : Fragment() {

    private lateinit var binding: FragmentFingerBinding
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator
    private val handler = Handler()
    private val handler1 = Handler(Looper.getMainLooper())
    private var result1: Int? = null
    private var isLightOn = false
    private var delayedTask: Runnable? = null
    private var led: Runnable? = null

    private var mediaPlayer: MediaPlayer? = null
    private val handlerSound = Handler(Looper.getMainLooper())
    private var isMusicPlaying = false

    private lateinit var animationView: LottieAnimationView

    private lateinit var soundJob: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFingerBinding.inflate(layoutInflater)
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Common.getVibration(requireContext())) {
            binding.btnVibration.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_vibrate_on
            )
        } else {
            binding.btnVibration.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_vibrate_off
            )
        }


        if (Common.getSound(requireContext())) {
            binding.btnSound.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_sound_on
            )
        } else {
            binding.btnSound.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_sound_off
            )
        }


        // On/Off vibration
        binding.btnVibration.setOnClickListener {
            Log.d("btn_vibrate", Common.getVibration(requireContext()).toString())
            if (Common.getVibration(requireContext())) {
                disableVibration()
                binding.btnVibration.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_vibrate_off
                )
                Log.d("btn_vibrate 1", Common.getVibration(requireContext()).toString())
            } else {
                enableVibration()
                binding.btnVibration.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_vibrate_on
                )
                Log.d("btn_vibrate 2", Common.getVibration(requireContext()).toString())
            }
        }

        binding.btnSound.setOnClickListener {
            Log.d("btn_sound", Common.getSound(requireContext()).toString())
            if (Common.getSound(requireContext())) {
                disableSound()
                binding.btnSound.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_sound_off
                )
                Log.d("btn_sound 1", Common.getSound(requireContext()).toString())

            } else {
                enableSound()
                binding.btnSound.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_sound_on
                )
                Log.d("btn_sound 2", Common.getSound(requireContext()).toString())
            }
        }

        delayedTask = Runnable {
            showDialog()
        }

        led = Runnable {
            startFlashingEffect()
        }

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.file_mp3)


        binding.btnFinger.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (Common.getVibration(requireContext())) {
                            startVibration()
                        }
                        if (Common.getSound(requireContext())) {

                            if (!isMusicPlaying) {
                            //    playSound()
                             soundJob =   CoroutineScope(Dispatchers.Default).launch {
                                    playSound()
                                }
                            }
                        }
                        handler.postDelayed(delayedTask!!, 5000)
                        handler.postDelayed(led!!, 0)

                        binding.gifAnim.visibility = View.GONE
                        binding.gifAnimScan.visibility = View.VISIBLE
                        binding.gifAnimScan.playAnimation()
                        binding.btnFinger.setAnimation(R.raw.js_finger_scan)
                        binding.imgText.text = getText(R.string.analyzing)

                        animationView = binding.gifAnim
                        animationView.progress = 0f
                        animationView.resumeAnimation()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        stopVibration()
                        soundJob.cancel()
                        stopSound()
                        handler.removeCallbacks(delayedTask!!)
                        handler.removeCallbacks(led!!)
                        handler1.removeCallbacksAndMessages(null)

                        binding.gifAnim.visibility = View.VISIBLE
                        binding.gifAnimScan.visibility = View.GONE
                        binding.btnFinger.setAnimation(R.raw.js_finger)
                        binding.imgText.text = getText(R.string.hold_to_scan)
                        return false
                    }
                }
                return false
            }
        })
    }

    private fun playSound() {
        try {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.file_mp3)
            mediaPlayer?.start() // Bắt đầu phát âm thanh
            Log.d("Sound_check","play sound")
            isMusicPlaying = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun stopSound() {
        //  mediaPlayer?.pause() // Tạm dừng âm thanh
        try {
            isMusicPlaying = false
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                Log.d("Sound_check","stop sound")
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
            Log.d("sound_error", e.printStackTrace().toString())
        }
    }

    private fun enableVibration() {
        // Check if the device supports vibration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(500)
        }
        Common.setVibration(requireContext(), true)
    }

    private fun disableVibration() {
        vibrator.cancel()

        Common.setVibration(requireContext(), false)
    }

    private fun enableSound() {

        Common.setSound(requireContext(), true)
    }

    private fun disableSound() {
        Common.setSound(requireContext(), false)
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
            val intent = Intent(requireActivity(), ResultActivity::class.java)
            intent.putExtra("result", result1)
            intent.putExtra("game", "one_player")
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
            binding.view.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_led_truth
            )
            binding.view1.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_led_not_tell
            )

        } else {
            binding.view.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_led_not_tell
            )
            binding.view1.background = ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_led_lie
            )
        }

        // Invert the light state
        isLightOn = !isLightOn
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
        mediaPlayer?.release()

    }
}





