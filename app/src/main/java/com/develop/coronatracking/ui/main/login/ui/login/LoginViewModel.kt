package com.develop.coronatracking.ui.main.login.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.develop.coronatracking.repo.FirebaseStorageHelper

class LoginViewModel : ViewModel() {

    fun login(userName: String){
        FirebaseStorageHelper.checkIfUserExists(userName)
    }

    fun getLoginLiveData(): MutableLiveData<Boolean> {
        return FirebaseStorageHelper.loginLiveData
    }
}
