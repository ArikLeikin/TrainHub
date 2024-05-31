package com.example.trainhub.models

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
                roomDatabase.postDao().insert(post)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit){
        postFireBaseModel.updatePostDocument(post){ isUpdatedInFireStore->
            if(isUpdatedInFireStore){
                roomDatabase.postDao().updatePost(post)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit){
        postFireBaseModel.deletePostDocument(post){ isDeletedFromFireStore->
            if(isDeletedFromFireStore){
                roomDatabase.postDao().deletePost(post)
                callback(true)
            }else{
                callback(false)
            }
        }
    }

    fun getAllPosts(callback: (List<Post>?) -> Unit){
        postFireBaseModel.getAllPostsDocument{ posts->
            if(posts.isNotEmpty()){
                roomDatabase.postDao().insertPostList(posts)
                callback(posts)
            }else{
                callback(null)
            }
        }
    }


}