package com.example.trainhub.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.UserModel
import com.example.trainhub.models.entities.User

class ProfileViewModel: ViewModel() {
    private val um: UserModel = UserModel.instance
    private val pm: PostModel = PostModel.instance

    private val _posts = MutableLiveData<List<PostWithUser>>()
    private val _user = MutableLiveData<User>()
    val posts: LiveData<List<PostWithUser>> get() = _posts
    val user: LiveData<User> get() = _user

    fun fetchPosts() {
        pm.getAllPosts { posts->
            val postWithUserList = mutableListOf<PostWithUser>()
            posts?.forEach { post->
                um.getUserById(post.userId) { user->
                    postWithUserList.add(PostWithUser(post, user))
                    _posts.value = postWithUserList
                }
            }
        }

    }

    fun updateProfile(user: User, callback: (Boolean) -> Unit){
       um.updateUser(user){isUpdated->
           if(isUpdated) {
               UserModel.instance.updateUser(user) { isLocalUpdateSuccessful ->
                   if (isLocalUpdateSuccessful) {
                       _user.value = user
                   }
               }
               callback(isUpdated)
           }
           else{
                callback(false)
           }
        }



    }



}