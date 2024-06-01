package com.example.trainhub.models.fireBaseModels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.room.RoomDatabase
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URI

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

    fun uploadImageToFireStorage(uri: Uri, imageFolder: String, callback: (Boolean)->Unit){
        val storageReference = storage.getReferenceFromUrl(STORAGE_PATH+imageFolder)
        val fileReference = storageReference.child("${System.currentTimeMillis()}.jpg")

        fileReference.putFile(uri)
            .addOnSuccessListener {
                Log.i(ContentValues.TAG, "uploadImageToFireStorage:success")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "uploadImageToFireStorage:failure", exception)
                callback(false)
            }
    }

}