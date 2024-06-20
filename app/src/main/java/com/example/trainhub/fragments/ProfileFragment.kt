package com.example.trainhub.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.adapters.PostRecyclerAdapter
import com.example.trainhub.viewModel.PostWithUser
import com.example.trainhub.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postsAdapter: PostRecyclerAdapter
    private var postsList: List<PostWithUser> = listOf()
    private val profileViewModel: ProfileViewModel = ProfileViewModel()
    private var name: TextView? = null
    private var email:TextView? = null
    private var profilePic:ImageView? = null
    private var btnEdit : Button? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

            name = view.findViewById(R.id.etName)
            email = view.findViewById(R.id.tvEmail)
            profilePic = view.findViewById(R.id.ivProfile)
            btnEdit = view.findViewById(R.id.btnEdit)


        val imageUrl = TrainHubApplication.Globals.currentUser?.profileImageUrl
        Log.d("ProfileFragment", "onCreateView: $imageUrl")
            email?.text = TrainHubApplication.Globals.currentUser?.email
             name?.text = TrainHubApplication.Globals.currentUser?.name


        if (imageUrl != null && imageUrl.startsWith("http")) {
            Glide.with(this).load(imageUrl).into(profilePic!!)
        } else {
            Glide.with(this).load(R.drawable.user_profile).into(profilePic!!)
        }

        btnEdit?.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_update_name, null)
            val pb: ProgressBar? = null
            val oldN = view.findViewById<EditText>(R.id.old_name)
            val newN = view.findViewById<EditText>(R.id.new_name)
            val builder = android.app.AlertDialog.Builder(context)
            oldN.setText(name?.text)
            val currentName = oldN.text.toString()
            oldN.isEnabled = false
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            view.findViewById<Button>(R.id.update_name_btn).setOnClickListener {
                pb?.visibility = View.VISIBLE
                val newName = newN.text.toString()
                if(newName.isEmpty()){
                    newN.error = "Name cannot be empty"
                    return@setOnClickListener
                }
                if(newName == currentName){
                    newN.error = "Name cannot be the same"
                    return@setOnClickListener
                }
                if (currentName == TrainHubApplication.Globals.currentUser?.name) {
                    val user = TrainHubApplication.Globals.currentUser
                    user?.name = newName
                    profileViewModel.updateProfile(user!!) { isUpdated ->
                        if (isUpdated) {
                            pb?.visibility = View.GONE
                            name?.text = newName
                            Toast.makeText(context, "Name updated successfully", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }



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



        return view

    }



}