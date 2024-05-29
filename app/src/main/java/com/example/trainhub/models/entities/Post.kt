package com.example.trainhub.models.entities

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "posts")
data class Post(@PrimaryKey var id:String,
                var title: String,
                var description: String,
                var imageUrl: String,
                var userId: String,
                var workoutCategory: String,
                var location: String,
                var lastUpdated: Long?=null){

        //TODO POST entity


}
