package com.example.trainhub.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainhub.MainActivity
import com.example.trainhub.R
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.adapters.PostRecyclerAdapter
import com.example.trainhub.viewModel.PostWithUser
import com.example.trainhub.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postsAdapter: PostRecyclerAdapter
    private var editProfile: TextView? = null
    private var postsList: List<PostWithUser> = listOf()
    private val profileViewModel: ProfileViewModel = ProfileViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerViewPosts = view.findViewById(R.id.rvMyPosts)
        recyclerViewPosts.layoutManager = LinearLayoutManager(context)
        postsAdapter = PostRecyclerAdapter(postsList,true)
        recyclerViewPosts.adapter = postsAdapter

        profileViewModel.fetchPosts()
        profileViewModel.posts.observe(viewLifecycleOwner) {posts->
            val userPosts = posts.filter { it.post.userId == TrainHubApplication.Globals.currentUser?.id }

            postsAdapter.posts = userPosts
            postsAdapter.notifyDataSetChanged()
        }

        editProfile = view.findViewById(R.id.edit_profile)
        editProfile?.setOnClickListener {
            val activity = context as MainActivity
            activity.moveToEditProfile()

        }

        return view

    }



}