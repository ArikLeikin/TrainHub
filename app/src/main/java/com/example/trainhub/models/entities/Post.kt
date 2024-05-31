package com.example.trainhub.models.entities

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.google.firebase.firestore.FieldValue
import java.io.Serializable

@Entity(tableName = "posts")
data class Post(@PrimaryKey var id:String,
                var title: String,
                var description: String,
                var imageUrl: String,
                var userId: String,
                var workoutCategory: String,
                var location: String,
                var lastUpdated: Long?=null):Serializable{

        //TODO POST entity
        companion object {
            const val ID_KEY = "id"
            const val TITLE_KEY = "title"
            const val DESCRIPTION_KEY = "description"
            const val IMAGE_URL_KEY = "imageUrl"
            const val USER_ID_KEY = "userId"
            const val WORKOUT_CATEGORY_KEY = "workoutCategory"
            const val LOCATION_KEY = "location"
            const val LAST_UPDATED = "lastUpdated"


        }
    fun fromJSON(json: Map<String, Any>) {
        id = json[ID_KEY] as? String ?: ""
        title = json[TITLE_KEY] as? String ?: ""
        description = json[DESCRIPTION_KEY] as? String ?: ""
        imageUrl = json[IMAGE_URL_KEY] as? String ?: ""
        userId = json[USER_ID_KEY] as? String ?: ""
        workoutCategory = json[WORKOUT_CATEGORY_KEY] as? String ?: ""
        location = json[LOCATION_KEY] as? String ?: ""

//        val post = Post(id,title,description,imageUrl,userId,workoutCategory,location)

        val timestamp: Long? = json[LAST_UPDATED] as? Long
        timestamp?.let {
            lastUpdated = it
        }
    }
    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                TITLE_KEY to title,
                DESCRIPTION_KEY to description,
                IMAGE_URL_KEY to imageUrl,
                USER_ID_KEY to userId,
                WORKOUT_CATEGORY_KEY to workoutCategory,
                LOCATION_KEY to location,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }


}
