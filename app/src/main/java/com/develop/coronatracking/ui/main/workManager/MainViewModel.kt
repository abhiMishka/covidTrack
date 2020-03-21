package com.develop.coronatracking.ui.main.workManager

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.develop.coronatracking.util.LocationWorkManager
import java.util.concurrent.TimeUnit


class MainViewModel : ViewModel() {

    private val LOCATION_WORKER_TAG = "location_worker_tag"
    var workManager: WorkManager = WorkManager.getInstance()
    private val locationGetterTask =
        PeriodicWorkRequestBuilder<LocationWorkManager>(15, TimeUnit.MINUTES).addTag(LOCATION_WORKER_TAG)

    private val locationLiveData = MutableLiveData<Location>()

    fun enqueWorker(){
        workManager.enqueueUniquePeriodicWork(
            LOCATION_WORKER_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            locationGetterTask.build()
        )
    }

    fun getLocationLiveData() : LiveData<Location>{
        return locationLiveData
    }
}
