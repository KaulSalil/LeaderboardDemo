package com.example.leaderboarddemo.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.leaderboarddemo.repository.LeaderboardRepository
import com.example.leaderboarddemo.ui.viewmodel.MainViewModel

class MainViewModelFactory(
    private val repository: LeaderboardRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MainViewModel(repository) as T
    }
}