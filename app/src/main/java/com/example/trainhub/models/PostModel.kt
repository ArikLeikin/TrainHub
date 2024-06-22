package com.example.trainhub.models

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
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
        postFireBaseModel.uploadImageToFireStorage(Uri.parse(post.imageUrl)){imageName->
            if(imageName!=null){
                post.imageUrl = imageName
                postFireBaseModel.addPostDocument(post){ uniqueId->
                    if(uniqueId!=null){
                        post.id = uniqueId
                        TrainHubApplication.Globals.executorService.execute{
                            roomDatabase.postDao().insert(post)
                        }
                        callback(true)
                    }else{
                        callback(false)
                    }
                }
            }else{
                callback(false)
            }

        }

    }

    fun updatePost(post: Post, hasNewImage:Boolean, callback: (Boolean) -> Unit){
        postFireBaseModel.uploadImageToFireStorage(Uri.parse(post.imageUrl)){imageName->
            if(imageName!=null){
                post.imageUrl = imageName
                postFireBaseModel.updatePostDocument(post){ isUpdatedInFireStore->
                    if(isUpdatedInFireStore){
                        TrainHubApplication.Globals.executorService.execute{
                            roomDatabase.postDao().updatePost(post)
                        }
                        callback(true)
                    }else{
                        Log.e(TAG, "Post not updated in FireStore!!!")
                        callback(false)
                    }
                }
            }else{
                Log.e(TAG, "Image not uploaded to FireStore!!!")
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
//                TrainHubApplication.Globals.executorService.execute{
//                    roomDatabase.postDao().insertPostList(posts)
//                }

                callback(posts)
            }else{
                println("No posts in FireStore OR NO INTERNET CONNECTION!!!")
                callback(null)
            }
        }
    }

    fun getAllPostsByCurrentUser( callback: (List<Post>?) -> Unit){
        TrainHubApplication.Globals.executorService.execute{
            //println(TrainHubApplication.Globals.currentUser)
            //callback(roomDatabase.postDao().getPostsByUserId(TrainHubApplication.Globals.currentUser!!.id))
            val preferences: SharedPreferences = TrainHubApplication.Globals.appContext!!.getSharedPreferences("user_session", Context.MODE_PRIVATE)
            preferences.getString("userId","")?.let {
                val posts = roomDatabase.postDao().getPostsByUserId(it)
                println(posts)
                callback(posts)
            }
        }
    }
    fun getPostById(postId: String, callback: (Post?) -> Unit){
        postFireBaseModel.getPostDocument(postId){ post->
            if(post!=null){
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