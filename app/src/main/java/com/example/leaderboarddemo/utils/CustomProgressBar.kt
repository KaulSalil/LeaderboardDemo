package com.example.leaderboarddemo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.leaderboarddemo.R

class CustomProgressBar(context: Context) : Dialog(context) {

    companion object {
        fun show(context: Context, cancelable: Boolean): Dialog {
            val dialog = Dialog(context)
            val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context), R.layout.dialog_custum_progress, null, false)
            dialog.setContentView(viewDataBinding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancelable)
            dialog.show()
            return dialog
        }
    }
}
