package com.example.trainhub.models

import com.example.trainhub.models.dao.AppLocalDatabase

class PostModel private constructor() {
    private val roomDatabase = AppLocalDatabase.db

    companion object {
        val instance: PostModel = PostModel()
    }

}