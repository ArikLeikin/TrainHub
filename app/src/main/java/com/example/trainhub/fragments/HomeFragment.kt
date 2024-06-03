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
import com.example.trainhub.databinding.FragmentHomeBinding
import com.example.trainhub.models.entities.Post
import com.example.trainhub.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    var postsRecyclerView: RecyclerView?=null
    var postAdapter: PostRecyclerAdapter?=null
    private var homeViewModel = HomeViewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        postsRecyclerView = binding.rvPostsList
        postsRecyclerView?.setHasFixedSize(true)
        homeViewModel.fetchPosts()
        homeViewModel.posts.observe(viewLifecycleOwner) {
            postAdapter?.posts= it
            postAdapter?.notifyDataSetChanged()
        }
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        postAdapter = PostRecyclerAdapter(listOf())

        postsRecyclerView?.adapter = postAdapter

        return view
    }
}
