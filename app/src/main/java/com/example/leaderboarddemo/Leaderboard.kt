package com.example.leaderboarddemo

import android.app.Application
import com.example.leaderboarddemo.repository.LeaderboardRepository
import com.example.leaderboarddemo.ui.viewmodelfactory.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class Leaderboard : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@Leaderboard))
        bind() from provider { LeaderboardRepository() }
        bind() from provider { MainViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
    }
}