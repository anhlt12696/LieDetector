package com.example.liedetector.ads

import android.app.Activity
import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import com.example.liedetector.activity.HomeActivity
import com.example.liedetector.base.Common
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import kotlin.math.roundToInt


object InAppReview {
    var ratingApp: Int = -1
     fun askUserForReview(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            try {
                if (task.isSuccessful) {
                    Log.i("inAppReview", "requestReviewFlow: Success ")
                    // We can get the ReviewInfo object
                    val reviewInfo = task.result
                    val flow =
                        manager.launchReviewFlow(
                            (activity as Activity?)!!, reviewInfo!!
                        )
                    flow.addOnCompleteListener { task2: Task<Void?>? ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Log.i("inAppReview", "launchReviewFlow: Success ")
                    }.addOnFailureListener { e: Exception? ->
                        // There was some problem, continue regardless of the result.
                        Log.i("inAppReview", "launchReviewFlow: Failed ")
                    }
                } else {
                    // There was some problem, continue regardless of the result.
                    val reviewErrorCode =
                        (task.exception as ReviewException?)!!.message
                    Log.i("inAppReview", "launchReviewFlow: Failed $reviewErrorCode")
                }
            } catch (ex: Exception) {
                Log.i("inAppReview", "requestReviewFlow Exception: $ex")
            }
        }.addOnFailureListener { e: Exception ->
            Log.i(
                "inAppReview",
                "requestReviewFlow Exception: $e"
            )
        }
    }
     fun dialogRateApp(layoutInflater : LayoutInflater, activity: Activity) {
        val customLayout = layoutInflater.inflate(R.layout.rate_dialog, null)
        val btn_submit = customLayout.findViewById<Button>(R.id.btn_submit)
        val btn_maybe_late = customLayout.findViewById<Button>(R.id.btn_maybe_late)
        val ratingBar: RatingBar = customLayout.findViewById(R.id.rt_bar)
        val icon = customLayout.findViewById<ImageView>(R.id.img_icon)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                // Handle rating change if needed
                ratingApp = ratingBar.rating.roundToInt()

                when(ratingApp){
                    0 -> icon.setImageResource(R.drawable.ic_no_star)
                    1 -> icon.setImageResource(R.drawable.ic_1_star)
                    2 -> icon.setImageResource(R.drawable.ic_2_star)
                    3 -> icon.setImageResource(R.drawable.ic_3_star)
                    4 -> icon.setImageResource(R.drawable.ic_4_star)
                    5 -> icon.setImageResource(R.drawable.ic_5_star)
                }
            }
        // Create the AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setView(customLayout)

         // Lấy WindowManager từ context của hoạt động hiện tại
         val windowManager = activity.getSystemService(WindowManager::class.java)

      /*   // Lấy thông tin về kích thước của cửa sổ hiện tại
         val displayMetrics = DisplayMetrics()
         windowManager.defaultDisplay.getMetrics(displayMetrics)
         val widthPixels = displayMetrics.widthPixels
         val heightPixels = displayMetrics.heightPixels*/

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.background_transparent)

        alertDialog.show()
      //   alertDialog.window?.setLayout(500, 600)
     //    alertDialog.window?.setLayout((widthPixels * 0.8f).toInt(), (heightPixels * 0.6f).toInt())
        alertDialog.setCancelable(false)
        btn_maybe_late.setOnClickListener {
            alertDialog.dismiss()
            Common.setInAppReview(activity, true)
        }
        btn_submit.setOnClickListener {
            Log.d("ratinggg",ratingApp.toString())
            if (ratingApp == 5){
                askUserForReview(activity)
                Common.setInAppReview(activity, true)
                alertDialog.dismiss()

            }else{
                alertDialog.dismiss()
                Common.setInAppReview(activity, true)
            }
        }
    }


}