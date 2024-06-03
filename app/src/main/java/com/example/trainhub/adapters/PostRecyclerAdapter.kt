package com.example.trainhub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.models.entities.Post

class PostRecyclerAdapter(var posts: List<Post>) :
    RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {

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
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
//        Glide.with(holder.itemView.context)
//            .load(currentItem.profileImageUrl)
//            .into(holder.profileImg)
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.postImg)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
