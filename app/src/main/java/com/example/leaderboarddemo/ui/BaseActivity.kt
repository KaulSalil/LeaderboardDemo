package com.example.leaderboarddemo.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.leaderboarddemo.R
import com.example.leaderboarddemo.utils.Utility
import com.examwarriors.notifiers.Loader
import com.examwarriors.notifiers.Notify
import com.examwarriors.notifiers.NotifyException
import com.examwarriors.notifiers.NotifyRetry
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein


abstract class BaseActivity  : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var baseBinding: ViewDataBinding
    var toolbar: Toolbar? = null
    var title: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!dataBinding) {
            setContentView(layoutResource)
        } else {
            baseBinding = DataBindingUtil.setContentView(this, layoutResource)
        }

        initializeViewModel()
        setBindings()
        getViewModel()?.let {
            it.notifier.receive { event ->
                when (event) {
                    is NotifyException -> {
                        event.exception.message?.let { msg ->
                        }
                    }
                    is Loader -> {
                        if (event.loading) {
                            Utility.showProgressDialog(this)
                        } else {
                            Utility.hideProgressDialog()
                        }
                    }
                    is NotifyRetry -> {
                    }
                    else -> {
                        onNotificationReceived(event)
                    }
                }
            }
        }


    }

    fun getBinding(): ViewDataBinding {
        return baseBinding
    }

    abstract val dataBinding: Boolean
    abstract val layoutResource: Int
    abstract fun getViewModel(): BaseViewModel?
    abstract fun onNotificationReceived(data: Notify)
    abstract fun setBindings()
    abstract fun initializeViewModel()

    inline fun <reified T> lazyBinding(): Lazy<T> = lazy { getBinding() as T }

    fun setToolBar(titleText: String) {
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.title)
        title?.maxLines = 1
        title?.ellipsize = TextUtils.TruncateAt.END
        title?.text = titleText
        toolbar?.setNavigationIcon(R.drawable.ic_back_arrow_black)
        toolbar?.contentInsetStartWithNavigation = 0
        setSupportActionBar(toolbar)

        toolbar?.setNavigationOnClickListener { finish() }

        toolbar?.navigationIcon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ContextCompat.getColor(
                    this,
                    R.color.white
                ), BlendModeCompat.SRC_ATOP
            )
    }
}