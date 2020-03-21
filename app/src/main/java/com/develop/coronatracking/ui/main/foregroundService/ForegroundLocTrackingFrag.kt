package com.develop.coronatracking.ui.main.foregroundService

import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.develop.coronatracking.R
import com.develop.coronatracking.ui.main.foregroundService.service.LocationUpdatesService
import com.develop.coronatracking.ui.main.foregroundService.service.LocationUpdatesService.LocalBinder
import com.develop.coronatracking.util.CommonUtils
import com.develop.coronatracking.util.LocationUtil
import kotlinx.android.synthetic.main.foreground_loc_tracking_fragment.*


class ForegroundLocTrackingFrag : Fragment() {

    private val TAG = ForegroundLocTrackingFrag::class.java.simpleName

    // Used in checking for runtime permissions.
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private var myReceiver: MyReceiver? = null

    // A reference to the service used to get location updates.
    private var mService: LocationUpdatesService? = null

    // Tracks the bound state of the service.
    private var mBound = false


    companion object {
        fun newInstance() =
            ForegroundLocTrackingFrag()
    }

    private lateinit var viewModel: ForegroundLocTrackingViewModel

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.i("testLocation", "onServiceConnected")

            val binder = service as LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i("testLocation", "onServiceDisconnected")

            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver =
            MyReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.foreground_loc_tracking_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        Log.i("testLocation", "onStart")

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        context?.bindService(
            Intent(context, LocationUpdatesService::class.java), mServiceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.i("testLocation", "onStop")

        if (mBound) {
            Log.i("testLocation", "unbind")

            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            context?.unbindService(mServiceConnection)
            mBound = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForegroundLocTrackingViewModel::class.java)

        if (CommonUtils.checkLocationPermission(activity!!)) {
            bindService()
        }

        myReceiver?.liveLocaData?.observe(viewLifecycleOwner, Observer {
            forlocationTv.text = it
        })

    }

    fun bindService() {
        Log.i("testLocation", "bindService")

        mService?.requestLocationUpdates()
        context?.bindService(
            Intent(context, LocationUpdatesService::class.java), mServiceConnection,
            BIND_AUTO_CREATE
        )
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    private class MyReceiver : BroadcastReceiver() {
        var liveLocaData = MutableLiveData<String>()
        override fun onReceive(context: Context?, intent: Intent) {
            Log.i("testLocation", "MyReceiver onReceive")

            val location: Location =
                intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                LocationUtil.getLocationText(location)?.let {
                    CommonUtils.showToast(it)
                    liveLocaData.value = it
                }
            }
        }
    }


}
