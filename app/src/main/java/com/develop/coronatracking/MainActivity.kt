package com.develop.coronatracking

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.develop.coronatracking.ui.main.foregroundService.ForegroundLocTrackingFrag
import com.develop.coronatracking.ui.main.login.LoginActivity
import com.develop.coronatracking.ui.main.usinglib.LibLocationWorkManager
import com.develop.coronatracking.ui.main.usinglib.LocationLibraryFragment
import com.develop.coronatracking.ui.main.workManager.MainFragment
import com.develop.coronatracking.util.CommonUtils
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance(), TAG_HOME)
//                .commitNow()

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, ForegroundLocTrackingFrag.newInstance(), TAG_FOREGROUND_FRAG)
//                .commitNow()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LocationLibraryFragment.newInstance(), TAG_LIB_FRAG)
                .commitNow()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    var frag = supportFragmentManager.findFragmentByTag(TAG_HOME)
                    if (frag != null && frag is MainFragment) {
                        frag.viewModel.enqueWorker()

                    } else {
                        frag = supportFragmentManager.findFragmentByTag(TAG_FOREGROUND_FRAG)
                        if (frag != null && frag is ForegroundLocTrackingFrag) {
                            frag.bindService()
                        }else{
                            frag = supportFragmentManager.findFragmentByTag(TAG_LIB_FRAG)
                            if (frag != null && frag is LocationLibraryFragment) {
                                frag.startTracking()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                CommonUtils.logOut()
                startLoginActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1000
        const val TAG_HOME = "TAG_HOME_FRAG"
        const val TAG_FOREGROUND_FRAG = "TAG_FOREGROUND_FRAG"
        const val TAG_LIB_FRAG = "TAG_LIB_FRAG"
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private val LIB_LOCATION_WORKER_TAG = "lib_location_worker_tag"
    var workManager: WorkManager = WorkManager.getInstance()
    private val locationGetterTask =
        PeriodicWorkRequestBuilder<LibLocationWorkManager>(16, TimeUnit.MINUTES).addTag(LIB_LOCATION_WORKER_TAG)

    fun enqueWorker(){
        workManager.enqueueUniquePeriodicWork(
            LIB_LOCATION_WORKER_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            locationGetterTask.build()
        )
    }

}
