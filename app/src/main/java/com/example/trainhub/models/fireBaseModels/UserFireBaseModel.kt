package com.example.trainhub.models.fireBaseModels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.trainhub.models.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONObject
import java.sql.Date

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

    fun getUserByID(id: String, callback: (User?) -> Unit) {
        db.collection(USERS_COLLECTION_PATH).whereEqualTo("id", id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result: QuerySnapshot? = task.result
                if (result != null && !result.isEmpty) {
                    val documentSnapshot = result.documents[0]

                    val jsonData: Map<String, Any>? = documentSnapshot.data
                    val jsonString = Gson().toJson(jsonData)
                    if (jsonData != null) {
                        Log.i(TAG, "JSON representation of the document: $jsonString")

                        val user = parseUserFromJson(jsonString)
                        callback(user)
                    } else {
                        Log.e(TAG, "Document data is null")
                        callback(null)
                    }
                } else {
                    Log.e(TAG, "No document found with ID: $id")
                    callback(null)
                }
            } else {
                Log.e(TAG, "getUser:failure", task.exception)
                callback(null)
            }
        }
    }

    fun getUser(email: String, callback: (User?) -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: QuerySnapshot? = task.result
                    val documentSnapshot = result?.documents?.get(0)

                    if (documentSnapshot != null) {
                        val jsonData: Map<String, Any>? = documentSnapshot.data
                        val jsonString = Gson().toJson(jsonData)
                        if (jsonData != null) {
                            Log.i(TAG, "JSON representation of the document: $jsonString")

                            val user = parseUserFromJson(jsonString)
                            callback(user)
                        } else {
                            Log.e(TAG, "Document data is null")
                            callback(null)
                        }
                    } else {
                        Log.e(TAG, "No document found with email: $email")
                        callback(null)
                    }
                } else {
                    Log.e(TAG, "getUser:failure", task.exception)
                    callback(null)
                }
            }
    }

    fun parseUserFromJson(jsonString: String): User? {
        val jsonObject = JSONObject(jsonString)
        val id = jsonObject.getString("id")
        val email = jsonObject.getString("email")
        val profileImageUrl = jsonObject.getString("profileImageUrl")
        val name = jsonObject.getString("name")
        val lastUpdatedJson = jsonObject.getJSONObject("lastUpdated")
        val seconds = lastUpdatedJson.getLong("seconds")
        val nanoseconds = lastUpdatedJson.getInt("nanoseconds")

        // Create a Date object using milliseconds
        val milliseconds = seconds * 1000 + nanoseconds / 1000000
        val lastUpdated = Date(milliseconds)

        return User(id, email, profileImageUrl, name, lastUpdated.time)
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

    fun uploadImageToFireStorage(uri: Uri, callback: (String?) -> Unit) {
        val imageFolder = "profile_images"
        super.uploadImageToFireStorage(uri, imageFolder) {
            callback(it)
        }
    }


}
