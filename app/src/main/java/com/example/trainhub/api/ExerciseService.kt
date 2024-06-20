package com.example.trainhub.api

import com.example.trainhub.models.entities.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExerciseService {
    @GET("exercises")
    suspend fun getExercises(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<List<Exercise>>
}
