package com.example.trainhub.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User


@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostList(posts: List<Post>)

    @Transaction
    @Query("SELECT * FROM posts where id = :id") //Note: id is user id or post id
    fun getPostByID(id: String): Post

    @Transaction
    @Query("SELECT * FROM posts where userId = :userId")
    fun getPostsByUserId(userId: String): List<Post>

    @Update
    fun updatePost(post: Post)

    @Delete
    fun deletePost(post: Post)
}