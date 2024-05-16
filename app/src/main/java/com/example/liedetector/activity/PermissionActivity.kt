package com.example.liedetector.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.liedetector.base.BaseActivity
import com.example.liedetector.base.Common
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ActivityPermissionBinding

class PermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding
    private var checkPermission : Boolean = false

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001


//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission granted, you can now initialize CameraX or perform other actions
//                initializeCameraX()
//                Common.setPerMission(this, true)
//                binding.swPermission.isChecked = true
//                binding.btnNext.isEnabled = true
//                binding.btnNext.background = ContextCompat.getDrawable(
//                    this,
//                    R.drawable.btn_continue_permit
//                )
//            } else {
//                // Permission denied, handle accordingly
//                Common.setPerMission(this,false)
//                binding.swPermission.isChecked = false
//                binding.btnNext.isEnabled = false
//                binding.btnNext.background = ContextCompat.getDrawable(
//                    this,
//                    R.drawable.btn_continue
//                )
//                binding.tvPermissionGranted.visibility = View.INVISIBLE
//                showPermissionDeniedDialog()
//            }
//        }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPermissionBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        checkPermission = Common.getPerMission(this)
//        Log.d("permission",checkPermission.toString())
//        binding.backArrow.setOnClickListener {
//            Common.setGamePlay(this, "one_player")
//            val intent = Intent(this@PermissionActivity,HomeActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.swPermission.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CAMERA
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                // Permission already granted, you can now initialize CameraX or perform other actions
//                // Example: initializeCameraX()
//            } else {
//                // Request camera permission
//                requestCameraPermission()
//                binding.tvPermissionGranted.visibility = View.VISIBLE
//                binding.btnNext.isEnabled = true
//                binding.btnNext.background = ContextCompat.getDrawable(
//                    this,
//                    R.drawable.btn_continue_permit
//                )
//            }
//        }
//
//      binding.btnNext.setOnClickListener {
//          finish()
//      }
//    }
//
//    private fun requestCameraPermission() {
//        // Request camera permission
//        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//    }
//
//    private fun initializeCameraX() {
//        // Implement CameraX initialization logic here
//        // Example: initialize CameraX preview or capture use cases
//    }
//
//    private fun showPermissionDeniedDialog() {
//        // Implement logic to show a dialog or message when the permission is denied
//    }
//}

    private val CAMERA_PERMISSION_DENIED_KEY = "camera_permission_denied"

    private fun setCameraPermissionDenied(isDenied: Boolean) {
        val sharedPreferences = getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(CAMERA_PERMISSION_DENIED_KEY, isDenied).apply()
    }

    private fun isCameraPermissionDenied(): Boolean {
        val sharedPreferences = getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(CAMERA_PERMISSION_DENIED_KEY, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission = Common.getPerMission(this)
        Log.d("permission",checkPermission.toString())
        binding.backArrow.setOnClickListener {
            Common.setGamePlay(this, "one_player")
            val intent = Intent(this@PermissionActivity,HomeActivity::class.java)
            startActivity(intent)
        }

        binding.swPermission.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked) {
                // Yêu cầu quyền truy cập máy ảnh từ người dùng
                requestCameraPermission()
            }


        }

        binding.btnNext.setOnClickListener {
            finish()
        }
    }
    private fun requestCameraPermission() {
        if (!isCameraPermissionDenied()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
            Log.d("permission__","ham xin quyen")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
                binding.tvPermissionGranted.visibility = View.VISIBLE
                binding.btnNext.isEnabled = true
                binding.swPermission.isChecked = true
                binding.btnNext.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.btn_continue_permit)
                Log.d("permission__","cap quyen")

            } else {
                // Quyền bị từ chối
                // Xử lý khi người dùng từ chối cấp quyền
                binding.tvPermissionGranted.visibility = View.INVISIBLE
                binding.btnNext.isEnabled = false
                binding.swPermission.isChecked = false
                binding.btnNext.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.btn_continue)
               displayDialogCamera()
                setCameraPermissionDenied(true)
                Log.d("permission__","tu choi")
            }
        }
    }
    private fun displayDialogCamera() {
        val builder = AlertDialog.Builder(this@PermissionActivity)
        builder.setMessage(resources.getString(R.string.per_mission_message_camera))
        builder.setTitle(resources.getString(R.string.per_mission_title_camera))
        builder.setCancelable(false)
        builder.setPositiveButton(
            resources.getString(R.string.permission_go_to_setting),
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 0)
            })
        builder.setNegativeButton(
            resources.getString(R.string.permission_cancel),
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.cancel() })
        val alertDialog = builder.create()
        alertDialog.show()
    }
    override fun onResume() {
        super.onResume()
        setCameraPermissionDenied(false)
    }
}
