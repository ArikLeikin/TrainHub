package com.example.trainhub.models.fireBaseModels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

abstract class FirebaseModel {

    protected val db = Firebase.firestore
    private val storage = Firebase.storage
    private val STORAGE_PATH = "gs://workoutapp-c040a.appspot.com/"

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        db.firestoreSettings = settings
    }

    fun uploadImageToFireStorage(uri: Uri, imageFolder: String, callback: (String?)->Unit){
        val storageReference = storage
            .getReference(imageFolder+"/"+System.currentTimeMillis()+".jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener {result->
                storageReference.downloadUrl.addOnSuccessListener { uriToStore->
                    Log.i(ContentValues.TAG, "uploadImageToFireStorage:success")
                    callback(uriToStore.toString())
                }.addOnFailureListener(){
                    Log.e(ContentValues.TAG, "uploadImageToFireStorage:failure", it)
                    callback(null)
                }
            .addOnFailureListener(){
                    Log.e(ContentValues.TAG, "uploadImageToFireStorage:failure", it)
                    callback(null)
                }

        }
    }

}