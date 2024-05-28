package com.example.trainhub.models.entities

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity
data class Post(@PrimaryKey var id:String){
        //TODO POST entity
}
