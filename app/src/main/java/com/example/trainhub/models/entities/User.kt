package com.example.trainhub.models.entities

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trainhub.TrainHubApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity(tableName = "users")
data class User(@PrimaryKey var id:String,
                var email: String,
                var profileImageUrl: String,
                var name: String,
                var lastUpdated: Long?=null){
    companion object {

        var lastUpdated: Long
            get() {
                return TrainHubApplication.Globals
                    .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                TrainHubApplication.Globals
                    ?.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                    ?.putLong(GET_LAST_UPDATED, value)?.apply()
            }


        const val ID_KEY = "id"
        const val EMAIL_KEY = "email"
        const val PROFILE_IMAGE_URL_KEY = "profileImageUrl"
        const val LAST_UPDATED = "lastUpdated"
        const val NAME_KEY = "name"
        const val GET_LAST_UPDATED = "get_last_updated"

        fun fromJSON(json: Map<String, Any>): User {
            val id = json[ID_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val profileImageUrl = json[PROFILE_IMAGE_URL_KEY] as? String ?: ""
            val name = json[NAME_KEY] as? String ?: ""

            val user = User(id,email,profileImageUrl,name)

            val timestamp: Timestamp? = json[LAST_UPDATED] as? Timestamp
            timestamp?.let {
                user.lastUpdated = it.seconds
            }

            return user
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                EMAIL_KEY to email,
                PROFILE_IMAGE_URL_KEY to profileImageUrl,
                NAME_KEY to name,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}
