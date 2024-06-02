package com.example.trainhub.viewModel

import androidx.lifecycle.ViewModel
import com.example.trainhub.models.PostModel
import com.example.trainhub.models.UserModel

class PostDetailsViewModel: ViewModel(){
    val postModel = PostModel.instance
}