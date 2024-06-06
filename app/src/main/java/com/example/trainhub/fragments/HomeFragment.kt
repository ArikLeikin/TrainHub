package com.example.trainhub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.trainhub.R
import com.example.trainhub.adapters.PostRecyclerAdapter
import com.example.trainhub.databinding.FragmentHomeBinding
import com.example.trainhub.viewModel.HomepageViewModel

class HomeFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener{

    private var postsRecyclerView: RecyclerView? = null
    private var postAdapter: PostRecyclerAdapter? = null
    private val homeViewModel = HomepageViewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var swipeRefresh:SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        postsRecyclerView = binding.rvPostsList
        postsRecyclerView?.setHasFixedSize(true)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        swipeRefresh = binding.swipeRefresh
        swipeRefresh?.setOnRefreshListener(this)

        homeViewModel.fetchPosts()
        homeViewModel.posts.observe(viewLifecycleOwner) {
            postAdapter?.posts = it
            postAdapter?.notifyDataSetChanged()
            swipeRefresh?.isRefreshing = false
        }

        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        postAdapter = PostRecyclerAdapter(listOf())
        postsRecyclerView?.adapter = postAdapter

        return view
    }

    override fun onRefresh() {
        homeViewModel.fetchPosts()

    }
}
