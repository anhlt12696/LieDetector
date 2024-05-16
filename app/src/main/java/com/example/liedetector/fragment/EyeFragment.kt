package com.example.liedetector.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.liedetector.activity.HomeActivity
import com.example.liedetector.activity.PermissionActivity
import com.example.liedetector.activity.ResultActivity
import com.example.liedetector.base.Common
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.FragmentEyeBinding
import android.Manifest
import kotlin.random.Random


class EyeFragment : Fragment() {

    private lateinit var binding: FragmentEyeBinding
    private var cameraFaceing: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private val handler = Handler()
    private var resultEye : Int? = null

    private var mediaPlayer: MediaPlayer? = null
    private val handlerSound = Handler(Looper.getMainLooper())
    private var isMusicPlaying = false

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEyeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Eye_check_sound","onViewCreated")
        startCamera(cameraFaceing)

        if(!checkCameraPermission()){
            val intent = Intent(activity, PermissionActivity::class.java)
            startActivity(intent)
        }


        binding.tmFlipCamera.setOnClickListener {
            flipCamera()
        }

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.file_mp3)

        binding.btnStart.setOnClickListener {
        //    binding.imEye.background = ContextCompat.getDrawable(requireContext(), R.drawable.img_eye_scanning)
            binding.tvAnalyzing.visibility = View.VISIBLE
            binding.btnStart.visibility = View.INVISIBLE
            binding.imEye.visibility = View.INVISIBLE
            binding.imEyeScan.visibility = View.VISIBLE
            playSoundResult()

            handler.postDelayed({
                showDialog()
            },5000)
        }

    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun flipCamera(){
        if (cameraFaceing == CameraSelector.DEFAULT_BACK_CAMERA) {
            cameraFaceing = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            cameraFaceing = CameraSelector.DEFAULT_BACK_CAMERA;
        }
        startCamera(cameraFaceing);
    }

    private fun startCamera(cameraFaceing: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                    Log.e("camera_eye", "camera preview set")
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraFaceing, preview)
                Log.e("camera_eye", "camera preview")

            } catch(exc: Exception) {
                Log.e("camera_eye", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
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
            resultEye  = (activity as? HomeActivity)?.result
            getResult()
            val intent = Intent(requireActivity(), ResultActivity::class.java)
            intent.putExtra("result",resultEye)
            intent.putExtra("game","eye")
            startActivity(intent)
            resultEye = null

        }
    }

    private fun  getResult() : Int? {
        if(resultEye == null){
            resultEye = Random.nextInt(4)
        }
        return resultEye
    }

    private fun playSoundResult() {
        if(Common.getSound(requireContext())){
            try {
                mediaPlayer?.start() // Bắt đầu phát âm thanh
                isMusicPlaying = true
                handlerSound.postDelayed({
                    stopSoundResult()
                }, 5000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun stopSoundResult() {
        //  mediaPlayer?.pause() // Tạm dừng âm thanh
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause() // Tạm dừng nhạc
                mediaPlayer!!.seekTo(0) // Quay lại đầu nhạc
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Xử lý hoặc ghi log lỗi nếu có
            Log.d("sound_error", e.printStackTrace().toString())
        }
        isMusicPlaying = false
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        handlerSound.removeCallbacksAndMessages(this)
        mediaPlayer!!.release()
        Log.d("Eye_check_sound","onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Eye_check_sound","onPause")
        handler.removeCallbacksAndMessages(null)
        handlerSound.removeCallbacksAndMessages(this)
        mediaPlayer!!.release()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Eye_check_sound","onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Eye_check_sound","onStart")
    }

    override fun onResume() {
        super.onResume()
        startCamera(cameraFaceing)
        Log.d("Eye_check_sound","onResume")
    }
}