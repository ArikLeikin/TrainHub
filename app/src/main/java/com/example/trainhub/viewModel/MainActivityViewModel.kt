package com.example.trainhub.viewModel

import androidx.lifecycle.ViewModel
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.UserModel

class MainActivityViewModel: ViewModel() {
    private var um: UserModel? = UserModel.instance
    fun setUser(userId: String, callback: (Boolean) -> Unit) {
        TrainHubApplication.Globals.executorService.execute {
            um?.getUserById(userId) {
                TrainHubApplication.Globals.currentUser = it
                callback(true)
            }
        }
    }

    fun logout() {
        um!!.logout(TrainHubApplication.Globals.currentUser!!)
    }
}