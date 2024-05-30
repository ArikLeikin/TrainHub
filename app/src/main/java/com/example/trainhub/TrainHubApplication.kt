package com.example.trainhub

import android.app.Application
import android.content.Context
//import com.getpet.Model.ModelRoom.AppLocalDB
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TrainHubApplication: Application()  {
    object Globals {
        var appContext: Context? = null
        private const val THREAD_AMOUNT = 4
        val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_AMOUNT)
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }
}