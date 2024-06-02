package com.example.trainhub

import android.app.Application
import android.content.Context
import com.example.trainhub.models.entities.User
//import com.getpet.Model.ModelRoom.AppLocalDB
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TrainHubApplication: Application()  {
    object Globals {
        var appContext: Context? = null
        private const val THREAD_AMOUNT = 4
        val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_AMOUNT)
        var currentUser: User?= null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }
}