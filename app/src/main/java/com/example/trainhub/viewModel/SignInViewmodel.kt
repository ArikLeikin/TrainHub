package com.example.trainhub.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trainhub.models.UserModel

class SignInViewmodel {
    private var um: UserModel? = UserModel.instance
    private val _signInStatus = MutableLiveData<Boolean>()
    val signInStatus: LiveData<Boolean> get() = _signInStatus

    fun signIn(email: String, password: String){
        Log.i(ContentValues.TAG, "viewModel signIn clicked")
        val isSuccess = um?.login(email, password) {
            _signInStatus.value = it
        }
    }
}