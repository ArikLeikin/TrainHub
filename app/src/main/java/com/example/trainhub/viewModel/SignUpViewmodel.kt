package com.example.trainhub.viewModel

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.UserModel
import com.example.trainhub.models.entities.User

class SignUpViewmodel: ViewModel() {
    private var um: UserModel? = null
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    init {
        um = UserModel.instance
    }

    fun register(user: User, password: String) {
        Log.i(TAG, "viewModel register clicked")
        val isSuccess = um?.register(user, password) {
            _registrationStatus.value = it
        }

    }
}