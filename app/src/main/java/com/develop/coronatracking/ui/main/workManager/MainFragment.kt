package com.develop.coronatracking.ui.main.workManager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.develop.coronatracking.R
import com.develop.coronatracking.util.CommonUtils
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.StringBuilder


class MainFragment : Fragment() {


    companion object {
        fun newInstance() =
            MainFragment()
    }

    lateinit var viewModel: MainViewModel
    var stringBuilder : StringBuilder = StringBuilder("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (CommonUtils.checkLocationPermission(activity!!)) {
            Log.i("testLocation", "got location permission")
            viewModel.enqueWorker()
        }

        viewModel.getLocationLiveData().observe(this, Observer {
            val msg = "lat : " +it.latitude + "  , long : " +it.longitude +"\n"
            stringBuilder.append(msg)
            CommonUtils.showToast(msg)
            locationTv.text = msg
            Log.i("testLocation", msg)

        })

    }




}
