package com.example.trainhub.models.fireBaseModels

import android.content.ContentValues.TAG
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.entities.Post
import com.google.firebase.firestore.Query
import java.util.LinkedList

class PostFireBaseModel: FirebaseModel(){
    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }
    fun addPostDocument(post: Post, callback:(Boolean) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).document(post.id).set(post.json)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    val location = TrainHubApplication.Globals.appContext?.let {context->
//                        Geocoder(context).getFromLocationName(post.location,1){
//                            it[0].latitude
//                            it[0].longitude
//                        }
//                    }
                    callback(true)
                } else {
                    Log.e(TAG, "Error adding post document: ${task.exception}")
                    callback(false)
                }
            }
    }

    fun updatePostDocument(post: Post, callback:(Boolean) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).document(post.id).set(post.json)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Error updating post document: ${task.exception}")
                    callback(false)
                }
            }
    }

    fun deletePostDocument(post: Post, callback:(Boolean) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).document(post.id).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Log.e(TAG, "Error deleting post document: ${task.exception}")
                    callback(false)
                }
            }
    }

    fun getAllPostsDocument(callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val posts: MutableList<Post> = mutableListOf()
                    for(document in task.result){
//                        Log.i(TAG, "Document: ${document.id} => ${document.data}")
                        val post = Post(document.id, "", "", "", "", "", "")
                        post.fromJSON(document.data)
                        posts.add(post)
                    }
                    callback(posts)
                } else {
                    Log.e(TAG, "Error getting all posts: ${task.exception}")
                    callback(listOf())
                }
            }
    }

    fun getPostsByUid(uid: String, callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .whereEqualTo("userId", uid)
            .get()
            .addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful) {
                val list = mutableListOf<Post>()
                for (document in snapshot.result) {
                    val post = Post(document.id, "", "", "", "", "", "" )
                    post.fromJSON(document.data)
                    list.add(post)
                }
                callback(list)
            } else {
                println("Error fetching posts for UID: $uid. Exception: $snapshot.exception")
                // Handle the case where the task was not successful
                callback(listOf())
            }
        }
    }

    fun getPostDocument(postId: String, callback: (Post?) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).document(postId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result.data
                    val post = Post(postId, "", "", "", "", "", "")
                    post.fromJSON(document!!)
                    callback(post)
                } else {
                    Log.e(TAG, "Error getting post document: ${task.exception}")
                    callback(null)
                }
            }
    }

    fun uploadImageToFireStorage(uri: Uri, callback: (String?) -> Unit) {
        val imageFolder = "posts_images"
        super.uploadImageToFireStorage(uri, imageFolder) { imageName ->
            callback(imageName)
        }
    }

}