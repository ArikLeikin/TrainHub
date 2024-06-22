package com.example.trainhub.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.trainhub.TrainHubApplication

class SharedPrefHelper(context: Context){
    private val PREFS_NAME = "UserPrefs"
    private val PROFILE_PIC_URI = "profile_pic_uri"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()


    fun saveProfilePicUri(uri: String) {
        editor.putString(PROFILE_PIC_URI, uri)
        editor.apply()
    }

    fun getProfilePicUri(): String? {
        Log.d("SharedPrefHelper", "getProfilePicUri: ${prefs.getString(PROFILE_PIC_URI, TrainHubApplication.Globals.currentUser?.profileImageUrl)}")
        return prefs.getString(PROFILE_PIC_URI, TrainHubApplication.Globals.currentUser?.profileImageUrl)
    }

    fun clearAll(){
        editor.clear()
        editor.apply()
    }

}