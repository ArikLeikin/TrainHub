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
import android.os.Bundle
import com.example.trainhub.MainActivity
import com.example.trainhub.fragments.PostDetailsFragment
class PostRecyclerAdapter(var posts: List<PostWithUser>,private var isClickable:Boolean) :
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
    fun updateUserProfilePic(newProfilePic: String) {
        for (postWithUser in posts) {
            postWithUser.user?.profileImageUrl = newProfilePic
        }
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = posts[position]
        holder.titleTextView.text = currentItem.user?.email
        holder.descriptionTextView.text = currentItem.post.description

        Glide.with(holder.itemView.context)
            .load(currentItem.post.imageUrl)
            .into(holder.postImg)

        Log.d(TAG,"POST IMAGE: posts_images/${currentItem.post.imageUrl}")

        Glide.with(holder.itemView.context)
            .load(currentItem.user?.profileImageUrl)
            .into(holder.profileImg)
        Log.d(TAG,"PROFILE IMAGE: profile_images/${currentItem.user?.profileImageUrl}")


        if (isClickable) {
            holder.itemView.setOnClickListener {
                Log.d(TAG, "Post clicked")
                val transaction = (holder.itemView.context as MainActivity).supportFragmentManager.beginTransaction()
                val frag = PostDetailsFragment()
                val bundle = Bundle()
                frag.arguments = bundle
                bundle.putString("postId", currentItem.post.id)
                transaction.replace(R.id.navHostMain, frag)
                transaction.addToBackStack(null)
                transaction.commit()


            }

        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
