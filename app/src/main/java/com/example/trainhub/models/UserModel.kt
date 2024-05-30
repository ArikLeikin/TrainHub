package com.example.trainhub.models

import android.util.Log
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.dao.AppLocalDatabase
import com.example.trainhub.models.entities.User
import com.example.trainhub.models.fireBaseModels.UserFireBaseModel

class UserModel private constructor() {
    private val roomDatabase = AppLocalDatabase.db
    private val userFireBaseModel = UserFireBaseModel()


    companion object {
        val instance: UserModel = UserModel()
    }

    fun register(user:User, password: String,callback: (Boolean) -> Unit){
        userFireBaseModel.register(user.email, password){ uid->
            if(uid!=null){
                // "" ->
                user.id = uid
                userFireBaseModel.addUserDocument(user){ isAddedToFireStore->
                    if(isAddedToFireStore){
                        callback(true)
                    }else{
                        Log.e("UserModel", "User Document not added to Firestore!!!")
                        userFireBaseModel.deleteUserAuth(){success->
                            if(success){
                                Log.d("UserModel", "User Auth deleted from Firebase")
                            }else{
                                Log.e("UserModel", "User Auth not deleted from Firebase!!!")
                            }
                            callback(false)
                        }
                    }
                }
            }
            else{
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
//    fun editProfile(user: User,callback: (Boolean) -> Unit ){
//        userFireBaseModel.editProfile(user){ isSuccessful ->
//            TrainHubApplication.Globals.executorService.execute{
//                roomDatabase.userDao().updateUser(user)
//            }
//            callback(isSuccessful)
//        }
//
//    }

    fun logout(user:User){
        TrainHubApplication.Globals.executorService.execute {
            roomDatabase.userDao().deleteUser(user)
        }
        //userFireBaseModel.logout()
    }
}