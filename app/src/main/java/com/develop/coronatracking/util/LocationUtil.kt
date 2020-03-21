package com.develop.coronatracking.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.preference.PreferenceManager
import android.util.Log
import com.develop.coronatracking.R
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.text.DateFormat
import java.util.*


class LocationUtil : KoinComponent {

    val context: Context by inject()

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var location: Location? = null
        if (isGPSEnabled()) {
            Log.i("Location Manager State", "GPS ENABLE")
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        return location
    }





    companion object {
        val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"

        /**
         * Returns true if requesting location updates, otherwise returns false.
         *
         * @param context The [Context].
         */
        fun requestingLocationUpdates(context: Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
        }

        /**
         * Stores the location updates state in SharedPreferences.
         * @param requestingLocationUpdates The location updates state.
         */
        fun setRequestingLocationUpdates(
            context: Context?,
            requestingLocationUpdates: Boolean
        ) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply()
        }

        /**
         * Returns the `location` object as a human readable string.
         * @param location  The [Location].
         */
        fun getLocationText(location: Location?): String? {
            return if (location == null) "Unknown location" else "(" + location.latitude + ", " + location.longitude + ")"
        }

        fun getLocationTitle(context: Context): String? {
            return context.getString(
                R.string.location_updated,
                DateFormat.getDateTimeInstance().format(Date())
            )
        }
    }
}