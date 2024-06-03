package com.example.trainhub.viewModel

import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.entities.Post

class AddPostViewModel : ViewModel(){
    val postModel = PostModel.instance

    fun addPost(post: Post, callback: (Boolean) -> Unit){
        postModel.addPost(post){isAdded->
            callback(isAdded)
        }
    }
}