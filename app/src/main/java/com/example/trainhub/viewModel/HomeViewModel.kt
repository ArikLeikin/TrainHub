package com.example.trainhub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.entities.Post

class HomeViewModel: ViewModel(){
    private val postModel = PostModel.instance
    private var _posts  = MutableLiveData<List<Post>>()
    val posts : LiveData<List<Post>> get() = _posts

    fun fetchPosts(){
        // Fetch posts from the database
        postModel.getAllPosts {
            _posts.value = it
        }

    }
}