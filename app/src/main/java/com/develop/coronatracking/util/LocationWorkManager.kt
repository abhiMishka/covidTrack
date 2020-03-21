package com.develop.coronatracking.util

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.develop.coronatracking.util.CommonUtils.Companion.showToast

class LocationWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val location = LocationUtil().getLocation()
        Log.i("testLocation","doing work")
        location?.let {
            sendLocation(location)
        }?:run{
            Log.i("testLocation","null location")
        }
        return Result.success()
    }

    private fun sendLocation(location: Location?) {
        val locationAvaibility = LocationUtil().isGPSEnabled()
        if (locationAvaibility) {
            location?.let {
                showToast(location.toString())
            }
        }
    }

}