package com.develop.coronatracking.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.develop.coronatracking.MainActivity
import com.develop.coronatracking.TopApplicationClass
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommonUtils {

    companion object{
        fun showToast(message : String){
            Toast.makeText(TopApplicationClass.instance,message,Toast.LENGTH_SHORT).show()
        }

        fun checkLocationPermission(context : Activity): Boolean {
            return if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    MainActivity.REQUEST_LOCATION_PERMISSION
                )
                false
            }
        }

        fun convertTime(timeInMillisec : Long) : String{
            val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            return formatter.format(timeInMillisec)
        }

        fun convertMilliSecToDate(timeInMillisec : Long) : String{
            val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            return formatter.format(timeInMillisec)
        }

        fun convertMilliSecToTime(timeInMillisec : Long) : String{
            val formatter: DateFormat = SimpleDateFormat("hh:mm:ss")
            return formatter.format(timeInMillisec)
        }

        fun isUserLoggedIn() : Boolean{
            return SharedPreferenceUtil.getData(SharedPreferenceUtil.KEY_USER_NAME,"")!=""
        }

        fun logOut(){
            SharedPreferenceUtil.saveData(SharedPreferenceUtil.KEY_USER_NAME,"")
        }
    }
}