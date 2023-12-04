package com.example.leaderboarddemo.ui.viewmodel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.viewModelScope
import com.example.leaderboarddemo.data.LeaderBoardJsonData
import com.example.leaderboarddemo.repository.LeaderboardRepository
import com.example.leaderboarddemo.ui.BaseViewModel
import com.example.leaderboarddemo.utils.LeaderboardException
import com.examwarriors.notifiers.Notify
import kotlinx.coroutines.launch

class MainViewModel(
private val repository: LeaderboardRepository
): BaseViewModel() {

    fun fetchLeaderboardData(context: Context, initial: Int, final: Int){

        viewModelScope.launch {
            notifier.notify(Notify(ON_START, ""))
            val splashTimeHandler = Handler()
                val response = repository.fetchDataFromJson(context, initial, final)
                response.let {
                    if (it is LeaderBoardJsonData) {
                        val finalizer = Runnable {
                            notifier.notify(Notify(ON_DATA_FETCH, it))
                        }
                        splashTimeHandler.postDelayed(finalizer, 1000)
                        return@launch
                    } else if (it is LeaderboardException) {
                        it.printStackTrace()
                        notifier.notify(Notify(ON_DATA_FAILURE, it.localizedMessage))
                    }
                }

        }

    }

    companion object {
        const val ON_DATA_FAILURE = "ON_DATA_FAILURE"
        const val ON_DATA_FETCH = "ON_DATA_FETCH"
        const val ON_START = "ON_START"
    }

}