package com.develop.coronatracking.ui.main.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.develop.coronatracking.R
import com.develop.coronatracking.ui.main.login.ui.login.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

}
