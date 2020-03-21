package com.develop.coronatracking

import android.app.Application

class TopApplicationClass : Application() {

    companion object {
        lateinit var instance: TopApplicationClass
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}