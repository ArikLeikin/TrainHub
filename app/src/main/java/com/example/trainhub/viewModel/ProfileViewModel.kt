package com.example.trainhub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.UserModel

class ProfileViewModel: ViewModel() {
    private val um: UserModel = UserModel.instance
    private val pm: PostModel = PostModel.instance

    private val _posts = MutableLiveData<List<PostWithUser>>()
    val posts: LiveData<List<PostWithUser>> get() = _posts

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



}