package com.example.trainhub.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.viewModel.PostWithUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostRecyclerAdapter(var posts: List<PostWithUser>) :
    RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {
    val storage = Firebase.storage
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageFilterView = itemView.findViewById(R.id.ifPostProfileImg)
        val titleTextView: TextView = itemView.findViewById(R.id.tvCardTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val postImg: ImageView = itemView.findViewById(R.id.ifPostImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = posts[position]
        holder.titleTextView.text = currentItem.post.title
        holder.descriptionTextView.text = currentItem.post.description

        Glide.with(holder.itemView.context)
            .load(currentItem.post.imageUrl)
            .into(holder.postImg)

        Log.d(TAG,"POST IMAGE: posts_images/${currentItem.post.imageUrl}")

        Glide.with(holder.itemView.context)
            .load(currentItem.user?.profileImageUrl)
            .into(holder.postImg)
        Log.d(TAG,"PROFILE IMAGE: profile_images/${currentItem.user?.profileImageUrl}")
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
