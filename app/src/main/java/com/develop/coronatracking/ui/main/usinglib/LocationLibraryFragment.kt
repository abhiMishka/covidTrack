package com.develop.coronatracking.ui.main.usinglib

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.develop.coronatracking.MainActivity
import com.develop.coronatracking.R
import com.develop.coronatracking.repo.FirebaseStorageHelper
import com.develop.coronatracking.repo.UserLocation
import com.develop.coronatracking.util.CommonUtils
import com.develop.coronatracking.util.SharedPreferenceUtil
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.item_location.*
import kotlinx.android.synthetic.main.location_library_fragment.*
import java.util.concurrent.TimeUnit


class LocationLibraryFragment : Fragment() {

    companion object {
        fun newInstance() =
            LocationLibraryFragment()
    }


    private lateinit var viewModel: LocationLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.location_library_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationLibraryViewModel::class.java)
        if (CommonUtils.checkLocationPermission(requireActivity())) {
           startTracking()
        }
    }

    fun startTracking(){
        (activity as MainActivity).enqueWorker()
        SmartLocation.with(context).location()
            .start {
                Log.i("testLocation", "starting ")

                FirebaseStorageHelper.addUserLocationData(UserLocation(
                    lat = it.latitude,
                    lng = it.longitude,
                    timeStamp = it.time,
                    formattedTime = CommonUtils.convertTime(it.time)
                ),
                    userId = SharedPreferenceUtil.getData(SharedPreferenceUtil.KEY_USER_NAME,"default"))
                Log.i("testLocation", "smartLoc : $it")
                fetchingLocationTv.visibility = View.GONE
                topCL.visibility = View.VISIBLE
                currentLocationTv.text = "lat: ${it.latitude}" +" long : ${it.longitude}"
                locationTimeTv.text = CommonUtils.convertTime(it.time)
            }
    }

}
