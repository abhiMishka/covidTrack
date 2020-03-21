package com.develop.coronatracking

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.develop.coronatracking.ui.main.foregroundService.ForegroundLocTrackingFrag
import com.develop.coronatracking.ui.main.usinglib.LocationLibraryFragment
import com.develop.coronatracking.ui.main.workManager.MainFragment

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

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1000
        const val TAG_HOME = "TAG_HOME_FRAG"
        const val TAG_FOREGROUND_FRAG = "TAG_FOREGROUND_FRAG"
        const val TAG_LIB_FRAG = "TAG_LIB_FRAG"
    }

}
