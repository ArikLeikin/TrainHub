package com.example.trainhub.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.models.entities.Exercise
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ExerciseAdapter(private val exercises: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvExerciseTitle)
        var bodyPart: TextView = itemView.findViewById(R.id.tvExerciseBodyPart)
        var equipment: TextView = itemView.findViewById(R.id.tvExerciseEquipment)
        var gifImage: ImageView = itemView.findViewById(R.id.ivExerciseImage)
        var name: TextView = itemView.findViewById(R.id.tvExerciseName)
        var target: TextView = itemView.findViewById(R.id.tvExerciseTarget)
        var secondaryMuscles: TextView = itemView.findViewById(R.id.tvExerciseSecondaryMuscles)
        var instructions: TextView = itemView.findViewById(R.id.tvExerciseInstructions)

    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
            return ExerciseViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
            val exercise = exercises[position]
            holder.title.text = "Exercise ${position + 1}"
            holder.bodyPart.text = "Body Part: ${exercise.bodyPart}"
            holder.equipment.text = "Equipment: ${exercise.equipment}"
            holder.name.text = "Name: ${exercise.name}"
            holder.target.text = "Target: ${exercise.target}"
            holder.secondaryMuscles.text =
                "Secondary Muscles: ${exercise.secondaryMuscles.joinToString(separator = ", ")}"
            holder.instructions.text = "Instructions: ${exercise.instructions.joinToString(separator = "\n")}"
            Glide.with(holder.gifImage.context).asGif().load(exercise.gifUrl).into(holder.gifImage)

        }


        override fun getItemCount() = exercises.size


    }



