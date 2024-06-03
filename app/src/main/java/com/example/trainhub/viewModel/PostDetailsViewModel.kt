package com.example.trainhub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.UserModel
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User

class PostDetailsViewModel: ViewModel(){
    private val postModel = PostModel.instance
    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post

    private val userModel = UserModel.instance
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _saveStatus = MutableLiveData<Boolean>()
    val saveStatus: LiveData<Boolean> get() = _saveStatus
    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> get() = _deleteStatus


    fun fetchPost(postId: String) {
        postModel.getPostById(postId) { currentPost ->
            _post.value = currentPost!!
            userModel.getUserById(currentPost.userId) { user ->
                _user.value = user!!
            }
        }
    }
    fun updatePost(updatedPost: Post, hasNewImage: Boolean) {
        postModel.updatePost(updatedPost,hasNewImage) { isUpdated ->
            _saveStatus.value = isUpdated
        }
    }

    fun deletePost(post:Post) {
        postModel.deletePost(post) { isDeleted ->
            _deleteStatus.value = isDeleted
        }
    }


}