package com.example.trainhub.models.entities

import com.google.gson.annotations.SerializedName

data class Exercise(
 val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val id: Int,
    val name: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)
