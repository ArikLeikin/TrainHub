package com.example.trainhub.models.fireBaseModels

import android.content.ContentValues.TAG
import android.util.Log
import com.example.trainhub.models.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class UserFireBaseModel: FirebaseModel() {

    companion object {
        val auth: FirebaseAuth by lazy {
            Firebase.auth
        }
        const val USERS_COLLECTION_PATH = "users"
    }

    fun register(email: String, password: String, callback: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid =  auth.currentUser?.uid
                    Log.i(TAG,"Registration success UID: $uid")
                    callback(uid)
                } else {
                    val exception = task.exception
                    Log.e(TAG,"Registration failed email: $email")
                    callback(null)
                }
            }
    }


    fun signIn(email: String, password: String, callback: (Boolean) -> Unit){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                Log.i(TAG, "signInWithEmail:success, UID: $uid")
                callback(true)
            } else {
                Log.e(TAG, "signInWithEmail:failure", task.exception)
                callback(false)
            }
        }
    }

    fun deleteUserAuth(callback: (Boolean) -> Unit){
        auth.currentUser?.delete()?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Log.i(TAG, "deleteUserAuth:success")
                callback(true)
            }else{
                Log.e(TAG, "deleteUserAuth:failed")
                callback(false)
            }
        }
    }

    fun getUser(email: String, callback: (User?) -> Unit){
        db.collection(USERS_COLLECTION_PATH).whereEqualTo("email", email).get().addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Log.i(TAG, "getUser:success")

                val result: QuerySnapshot? = task.result
                var user: User? = result?.documents?.get(0)?.toObject(User::class.java)
                callback(user)
            }else{
                Log.e(TAG, "getUser:failure", task.exception)
                callback(null)
            }
        }
    }

    fun addUserDocument(user:User, callback: (Boolean) -> Unit){
        db.collection(USERS_COLLECTION_PATH).document(user.id).set(user.json).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.i(TAG, "addUser:success")
                callback(true)
            }else{
                Log.e(TAG, "addUser:failed", task.exception)
                callback(false)
            }
        }
    }

    fun updateUserDocument(user:User, callback: (Boolean) -> Unit){
        db.collection(USERS_COLLECTION_PATH).document(user.id).update(user.json).addOnCompleteListener {task ->
            if (task.isSuccessful){
                Log.i(TAG, "editProfile:success UID: ${user.id}")
                callback(true)
            }else{
                Log.e(TAG, "editProfile:failure UID: ${user.id}")
                callback(false)
            }
        }
    }



}
