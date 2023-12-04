package com.example.leaderboarddemo.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class Utility {
    companion object {
        private var progressInstance: Dialog? = null

        fun showProgressDialog(activity: Activity, isCancelable: Boolean = false) {
            progressInstance?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
            try {
                progressInstance = CustomProgressBar.show(activity, isCancelable)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideProgressDialog() {
            try {
                progressInstance?.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
