package com.example.trainhub.models

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.dao.AppLocalDatabase
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.fireBaseModels.PostFireBaseModel

class PostModel private constructor() {
    private val roomDatabase = AppLocalDatabase.db
    private val postFireBaseModel = PostFireBaseModel()


    companion object {
        val instance: PostModel = PostModel()
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit){
        postFireBaseModel.addPostDocument(post){ isAddedToFireStore->
            if(isAddedToFireStore){
                postFireBaseModel.uploadImageToFireStorage(Uri.parse(post.imageUrl)) { isUploaded ->
                    if (isUploaded) {
                        TrainHubApplication.Globals.executorService.execute{
                            roomDatabase.postDao().insert(post)
                        }
                        callback(true)
                    } else {
                        postFireBaseModel.deletePostDocument(post) { isDeletedFromFireStore ->
                            if (isDeletedFromFireStore) {
                                Log.i(TAG, "Image not uploaded to FireStore, Post deleted from FireStore")
                                callback(false)
                            } else {
                                Log.e(TAG, "Image not uploaded to FireStore, Post not deleted from FireStore!!!")
                                callback(false)
                            }
                            callback(false)
                        }
                    }
                }
            }else{
                callback(false)
            }
        }
    }

    fun updatePost(post: Post, hasNewImage:Boolean, callback: (Boolean) -> Unit){
        postFireBaseModel.updatePostDocument(post){ isUpdatedInFireStore->
            if(isUpdatedInFireStore){
                if(hasNewImage){
                    postFireBaseModel.uploadImageToFireStorage(Uri.parse(post.imageUrl)) { isUploaded ->
                        if (isUploaded) {
                            TrainHubApplication.Globals.executorService.execute{
                                roomDatabase.postDao().updatePost(post)
                            }
                            callback(true)
                        } else {
                            Log.e(TAG, "Image not uploaded to FireStore!!!")
                            callback(false)
                        }
                    }
                }
                TrainHubApplication.Globals.executorService.execute{
                    roomDatabase.postDao().updatePost(post)
                }
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit){
        postFireBaseModel.deletePostDocument(post){ isDeletedFromFireStore->
            if(isDeletedFromFireStore){
                TrainHubApplication.Globals.executorService.execute{
                    roomDatabase.postDao().deletePost(post)
                }
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun getAllPosts(callback: (List<Post>?) -> Unit){
        postFireBaseModel.getAllPostsDocument{ posts->
            if(posts.isNotEmpty()){
                TrainHubApplication.Globals.executorService.execute{
                    roomDatabase.postDao().insertPostList(posts)
                }
                callback(posts)
            }else{
                callback(null)
            }
        }
    }

    fun getPostById(postId: String, callback: (Post?) -> Unit){
        postFireBaseModel.getPostDocument(postId){ post->
            if(post!=null){
                //TODO: check if post is already in room database and if post is user's post

//                TrainHubApplication.Globals.executorService.execute{
//                    roomDatabase.postDao().insert(post)
//                }
                callback(post)
            }else{
                callback(null)
            }
        }
    }


}