package com.develop.coronatracking.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.develop.coronatracking.util.CommonUtils
import com.develop.coronatracking.util.SharedPreferenceUtil
import com.develop.coronatracking.util.SharedPreferenceUtil.KEY_USER_NAME
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseStorageHelper {
    val db = Firebase.firestore
    const val LOCATIONS_COLLECTION = "locations_collection"
    const val USER_COLLECTION = "user_collection"
    const val DOCUMENT_LOCATIONS = "locations"
    const val TAG = "testLocation"

    var loginLiveData = MutableLiveData<Boolean>()
        get() = field

    fun addUserLocationData(userLocation: UserLocation,userId : String){
        Log.i("testLocation", "adding document")
        db.collection(LOCATIONS_COLLECTION).document(DOCUMENT_LOCATIONS).collection(userId)
            .document(CommonUtils.convertMilliSecToDate(System.currentTimeMillis()))
            .collection(CommonUtils.convertMilliSecToTime(System.currentTimeMillis()))
            .add(userLocation)
            .addOnSuccessListener { data ->
                Log.i("testLocation", "DocumentSnapshot added with $data")
            }
            .addOnFailureListener { e ->
                Log.i("testLocation", "Error adding document", e)
            }
    }


    fun checkIfUserExists(userName : String) {
        val docRef = db.collection(USER_COLLECTION).document(userName)

        val source = Source.SERVER

        docRef.get(source)
            .addOnSuccessListener { document ->
                //todo figure out why document if not null is userName doesn't exist
                if (document != null && document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    SharedPreferenceUtil.saveData(KEY_USER_NAME,userName)
                    loginLiveData.postValue(true)
                } else {
                    Log.d(TAG, "No such document")
                    loginLiveData.postValue(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                loginLiveData.postValue(false)
            }


    }


}