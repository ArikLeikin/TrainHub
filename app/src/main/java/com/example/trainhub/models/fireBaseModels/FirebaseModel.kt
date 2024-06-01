package com.example.trainhub.models.fireBaseModels

import android.net.Uri
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URI

abstract class FirebaseModel {

    protected val db = Firebase.firestore
    protected val storage = Firebase.storage

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        db.firestoreSettings = settings
    }

    abstract fun uploadImageToFireStorage(uri: Uri)

}