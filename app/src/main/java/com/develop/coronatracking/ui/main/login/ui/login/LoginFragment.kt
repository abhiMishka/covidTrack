package com.develop.coronatracking.ui.main.login.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.develop.coronatracking.MainActivity
import com.develop.coronatracking.R
import com.develop.coronatracking.util.CommonUtils
import com.develop.coronatracking.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var signInLiveData: MutableLiveData<Boolean>
    private lateinit var viewModel: LoginViewModel
    lateinit var userName : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        if(!CommonUtils.isUserLoggedIn()) {

            signInLiveData = viewModel.getLoginLiveData()

            loginButton.setOnClickListener {
                userName = registrationCodeEt.text.toString()
                if (!userName.isBlank()) {
                    viewModel.login(registrationCodeEt.text.toString())
                } else {
                    displayNumberErrorMessage(R.string.invalid_regis_code)
                }
            }

            signInLiveData.observe(viewLifecycleOwner, Observer {
                if (it) {
                    startLandingActivity()
                } else {
                    displayNumberErrorMessage(R.string.user_doesnt_exist)
                }
            })
        }else{
            startLandingActivity()
        }

    }

    private fun startLandingActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun displayNumberErrorMessage(stringResId: Int) {
        registrationCodeEt.background =
            ContextCompat.getDrawable(registrationCodeEt.context, R.drawable.error_bg_edit_text)
        error_message.visibility = View.VISIBLE
        error_message.text = getString(stringResId)
        val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
        registrationCodeEt.startAnimation(shake)
    }

}
