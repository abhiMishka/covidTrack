package com.develop.coronatracking.ui.main.usinglib

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.develop.coronatracking.TopApplicationClass
import com.develop.coronatracking.repo.FirebaseStorageHelper
import com.develop.coronatracking.repo.UserLocation
import com.develop.coronatracking.util.CommonUtils
import com.develop.coronatracking.util.LocationUtil
import com.develop.coronatracking.util.SharedPreferenceUtil
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.LocationProvider
import io.nlopez.smartlocation.location.providers.MultiFallbackProvider

class LibLocationWorkManager(cntx: Context, workerParams: WorkerParameters) :
    Worker(cntx, workerParams) {
    override fun doWork(): Result {

        val fallbackProvider: LocationProvider = MultiFallbackProvider.Builder()
            .withGooglePlayServicesProvider().build()
        SmartLocation.with(TopApplicationClass.instance).location(fallbackProvider)
            .start {
                Log.i("testLocation", "lib starting ")

                FirebaseStorageHelper.addUserLocationData(
                    UserLocation(
                        lat = it.latitude,
                        lng = it.longitude,
                        timeStamp = it.time,
                        formattedTime = CommonUtils.convertTime(it.time)
                    ),
                    userId = SharedPreferenceUtil.getData(SharedPreferenceUtil.KEY_USER_NAME,"default"))
                Log.i("testLocation", "lib smartLoc : $it")
            }
        return Result.success()
    }


}


