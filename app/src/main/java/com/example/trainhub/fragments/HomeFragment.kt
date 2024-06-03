package com.example.trainhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainhub.R
import com.example.trainhub.adapters.PostRecyclerAdapter
import com.example.trainhub.models.entities.Post

class HomeFragment : Fragment() {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postsRecyclerView = view.findViewById(R.id.rvPostsList)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize your post list and adapter here
        val postsList = listOf(
            Post("Post 1", "Title 1", "Description let fill it with random words and see how it looks 1","android.resource://${requireActivity().packageName}/${R.drawable.background}","","",""),
            Post("Post 2", "Title 2", "Description let fill it with random words and see how it looks 2","android.resource://${requireActivity().packageName}/${R.drawable.background}","","","")
            // Add more posts as needed
        )

        postAdapter = PostRecyclerAdapter(postsList)
        postsRecyclerView.adapter = postAdapter

        return view
    }
}
