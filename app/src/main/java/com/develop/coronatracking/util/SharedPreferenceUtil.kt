package com.develop.coronatracking.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.develop.coronatracking.TopApplicationClass
import com.google.gson.Gson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

object SharedPreferenceUtil : KoinComponent {
    val gson : Gson by inject()
    val DEFAULT_PREF_NAME_FOR_NON_DELETING_PREFS = "MyPrefsForNonDeletingPrefs"
    val KEY_USER_NAME = "user_name"


    private val sharedPreferences: SharedPreferences =
        TopApplicationClass.instance.applicationContext.getSharedPreferences(
            DEFAULT_PREF_NAME_FOR_NON_DELETING_PREFS, Activity.MODE_PRIVATE
        )

    private var mSharedPreferencesForNonDeletingPref: SharedPreferences  = TopApplicationClass.instance.applicationContext.getSharedPreferences(DEFAULT_PREF_NAME_FOR_NON_DELETING_PREFS,Activity.MODE_PRIVATE)


    private var editor = sharedPreferences.edit()
    private var editorForNonDeletingPrefs= mSharedPreferencesForNonDeletingPref.edit()


    fun saveData(key: String, value: String) {
        editor.putString(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Long) {
        editor.putLong(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Float) {
        editor.putFloat(key, value)
        return editor.apply()
    }

    fun saveData(key: String, value: Int) {
        editor.putInt(key, value)
        return editor.apply()
    }


    fun getData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = gson.toJson(`object`)
        //Save that String in SharedPreferences
        saveData(key, jsonString)
    }

    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value : String = getData(key, "")
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return try {
            gson.fromJson(value, T::class.java) as T
        } catch (exception : Exception) {
            null
        }
    }



    fun deleteAllData() {
        editor.clear()
        editor.apply()
    }

    fun getDataForNonDeletingPrefs(
        key: String,
        defaultValue: String
    ): String? {
        return mSharedPreferencesForNonDeletingPref.getString(key, defaultValue)
    }

    fun getDataForNonDeletingPrefs(
        key: String,
        defaultValue: Boolean
    ): Boolean {
        return mSharedPreferencesForNonDeletingPref.getBoolean(key, defaultValue!!)
    }

    fun getDataForNonDeletingPrefs(key: String, defaultValue: Int): Int {
        return mSharedPreferencesForNonDeletingPref.getInt(key, defaultValue)
    }

    fun saveDataForNonDeletingPref(
        key: String,
        value: String
    ): Boolean {
        editorForNonDeletingPrefs.putString(key, value)
        return editorForNonDeletingPrefs.commit()
    }


    fun saveDataForNonDeletingPref(key: String, value: Int): Boolean {
        editorForNonDeletingPrefs.putInt(key, value)
        return editorForNonDeletingPrefs.commit()
    }

    fun saveDataForNonDeletingPref(
        key: String,
        value: Boolean
    ): Boolean {
        editorForNonDeletingPrefs.putBoolean(key, value)
        return editorForNonDeletingPrefs.commit()
    }


}