package com.example.trainhub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.UserModel
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User

class HomepageViewModel : ViewModel() {
    private val um: UserModel = UserModel.instance
    private val pm: PostModel = PostModel.instance

    private val _posts = MutableLiveData<List<PostWithUser>>()
    val posts: LiveData<List<PostWithUser>> get() = _posts

    fun fetchPosts() {
        pm.getAllPosts { postList ->
            val postsWithUsers = mutableListOf<PostWithUser>()
            if (postList != null) {
                postList.forEach { post ->
                    um.getUserById(post.userId) { user ->
                        postsWithUsers.add(PostWithUser(post, user))
                        if (postsWithUsers.size == postList.size) {
                            _posts.value = postsWithUsers
                        }
                    }
                }
            }
        }
    }
}

data class PostWithUser(
    val post: Post,
    val user: User?
)
