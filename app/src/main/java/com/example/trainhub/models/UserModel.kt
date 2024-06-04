package com.example.trainhub.models

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.fragments.LoginFragment
import com.example.trainhub.models.dao.AppLocalDatabase
import com.example.trainhub.models.entities.User
import com.example.trainhub.models.fireBaseModels.UserFireBaseModel

class UserModel private constructor() {
    private val roomDatabase = AppLocalDatabase.db
    private val userFireBaseModel = UserFireBaseModel()

    private val preferences = TrainHubApplication.Globals.appContext!!.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        val instance: UserModel = UserModel()
    }

    fun register(user:User, password: String,callback: (Boolean) -> Unit){
        userFireBaseModel.register(user.email, password){ uid->
            if(uid!=null){
                // "" ->
                user.id = uid
                userFireBaseModel.uploadImageToFireStorage(Uri.parse(user.profileImageUrl)) { result ->
                    if (result != null) {
                        user.profileImageUrl = result
                        userFireBaseModel.addUserDocument(user){ isAddedToFireStore->
                            if(isAddedToFireStore){

                            }else{
                                Log.e(TAG, "User Document not added to Firestore!!!")
                                userFireBaseModel.deleteUserAuth(){success->
                                    if(success){
                                        Log.i(TAG, "User Auth deleted from Firebase")
                                    }else{
                                        Log.e(TAG, "User Auth not deleted from Firebase!!!")
                                    }
                                    callback(false)
                                }
                            }
                        }
                        Log.i(TAG, "User Document added Image to Firestore")
                    } else {
                        Log.e(TAG, "User Document Image not added to Firestore!!!")
                    }
                    callback(true)
                }
            }
            else{
                Log.i(TAG, "User Auth not created in Firebase")
                callback(false)
            }
        }
    }

    fun login(email: String, password: String, callback: (Boolean) -> Unit){
        userFireBaseModel.signIn(email, password){ isAuthenticated->
            if(isAuthenticated){
                userFireBaseModel.getUser(email){ user->
                    if(user!=null){
                        TrainHubApplication.Globals.executorService.execute {
                            roomDatabase.userDao().insert(user)
                        }

                        val editor = preferences.edit()
                        editor.putBoolean("is_logged_in", true)
                        editor.putString("userId", user.id)
                        // Add more data as needed
                        editor.apply()
                        TrainHubApplication.Globals.currentUser = user
                        callback(true)
                    }
                    else{
                        Log.e("UserModel", "User not found in Firestore")
                        callback(false)
                    }
                }
            }
            else{
                callback(false)
            }

        }

    }
    fun updateUser(user: User,callback: (Boolean) -> Unit ){
        userFireBaseModel.updateUserDocument(user){ isSuccessful ->
            TrainHubApplication.Globals.executorService.execute{
                roomDatabase.userDao().updateUser(user)
            }
            callback(isSuccessful)
        }

    }

    fun logout(user:User){
        TrainHubApplication.Globals.currentUser = null
        val editor = preferences.edit()
        editor.putBoolean("is_logged_in", false)
        editor.putString("userId", "")
        editor.apply()
        editor.clear()
        TrainHubApplication.Globals.executorService.execute {
            roomDatabase.userDao().deleteUser(user)
        }
    }

    fun getUserById(id: String, callback: (User?) -> Unit){
        userFireBaseModel.getUserByID(id){user->
            if (user != null){
                Log.i(TAG, "getUserById successful ID: $id")
                callback(user)
            }else{
                Log.e(TAG, "getUserById failed ID: $id")
                callback(null)
            }
        }
    }
}