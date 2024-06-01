package com.example.trainhub


import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.fireBaseModels.PostFireBaseModel
import org.junit.Test
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PostFirebaseModelTask{

    private val PostFireBaseModel = PostFireBaseModel()
    private val id = "Another Test"
    private val title = "asdfasd Test 2"
    private val description = " asdfdasf updated 2"
    private val imageUrl = " asdfasdf Test "
    private val userId = "Test User Id"
    private val workoutCategory = "Test Workout Category ddd 2"
    private val location = "Test Location ddasd 2"
    private val lastUpdated: Long? = null

    @Test
    fun addPost(){
        val post = Post(id,title, description, imageUrl, userId, workoutCategory, location)
        var result: Boolean? = null
        val latch = CountDownLatch(1)

        PostFireBaseModel.addPostDocument(post){ isAddedToFireStore->
            result = isAddedToFireStore
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS)
        assertEquals(true, result)
    }

    @Test
    fun updatePost(){
        val post = Post("test id",title, description, imageUrl, userId, workoutCategory, location)
        var result: Boolean? = null
        val latch = CountDownLatch(1)

        PostFireBaseModel.updatePostDocument(post){ isUpdatedInFireStore->
            result = isUpdatedInFireStore
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS)
        assertEquals(true, result)
    }

    @Test
    fun deletePost(){
        val post = Post("test id",title, description, imageUrl, userId, workoutCategory, location)
        var result: Boolean? = null
        val latch = CountDownLatch(1)

        PostFireBaseModel.deletePostDocument(post){ isDeletedFromFireStore->
            result = isDeletedFromFireStore
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS)
        assertEquals(true, result)
    }

    @Test
    fun getAllPosts(){
        var result: List<Post>? = null
        val latch = CountDownLatch(1)

        PostFireBaseModel.getAllPostsDocument{ posts->
            result = posts
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS)
        println("TEST GET ALL POSTS")
        println("-------------------")
        println(result)
        println("-------------------")
        assertNotNull(result)
    }

    @Test
    fun getPostsByUid(){
        var result: List<Post>? = null
        val latch = CountDownLatch(1)

        PostFireBaseModel.getPostsByUid(userId){ posts->
            result = posts
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS)
        println("TEST GET ALL POSTS OF USER ID")
        println("-------------------")
        println(result)
        println(result?.size)
        println("-------------------")
        assertNotNull(result)
    }


}