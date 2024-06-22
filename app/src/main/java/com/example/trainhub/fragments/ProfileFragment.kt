package com.example.trainhub.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trainhub.MainActivity
import com.example.trainhub.R
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.adapters.PostRecyclerAdapter
import com.example.trainhub.models.entities.User
import com.example.trainhub.services.SharedPrefHelper
import com.example.trainhub.viewModel.PostWithUser
import com.example.trainhub.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var pb: ProgressBar? = null
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postsAdapter: PostRecyclerAdapter
    private var postsList: List<PostWithUser> = listOf()
    private val profileViewModel: ProfileViewModel = ProfileViewModel()
    private var name: TextView? = null
    private var email:TextView? = null
    private var profilePic:ImageView? = null
    private var btnEdit : Button? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var filePath: String? = null
    private var userId: String? = null
    private var lastUpdated: Long = 0
    private var logout:TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        pb = view.findViewById(R.id.pbUpdateProfile)
        userId = TrainHubApplication.Globals.currentUser?.id
        name = view.findViewById(R.id.etName)
        email = view.findViewById(R.id.tvEmail)
        profilePic = view.findViewById(R.id.ivProfile)
        btnEdit = view.findViewById(R.id.btnEdit)
        logout = view.findViewById(R.id.tvlogout)
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){loadedProfileImageInPickerHandler(it)}

        var sharedPrefHelper = SharedPrefHelper(requireContext())
        email?.text = TrainHubApplication.Globals.currentUser?.email
        name?.text = TrainHubApplication.Globals.currentUser?.name
        lastUpdated = TrainHubApplication.Globals.currentUser?.lastUpdated!!


        logout?.setOnClickListener {
            Log.d("ProfileFragment", "Logout clicked")
            Log.d("ProfileFragment", "Current user: ${TrainHubApplication.Globals.currentUser}")

            profileViewModel.handleLogout(TrainHubApplication.Globals.currentUser!!) {
                sharedPrefHelper = SharedPrefHelper(requireContext())
                sharedPrefHelper.clearAll()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }


        val savedImageUri = sharedPrefHelper.getProfilePicUri()
        Log.d("ProfileFragment", "Saved Image URI: $savedImageUri")
        if (savedImageUri != null) {
            Glide.with(this).load(savedImageUri).into(profilePic!!)
        } else {
            Glide.with(this).load(R.drawable.user_profile).into(profilePic!!)
        }

        btnEdit?.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_update_name, null)
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
                    profileViewModel.updateProfile(user!!,false) { isUpdated ->
                        if (isUpdated) {
                            name?.text = newName
                            Toast.makeText(context, "Name updated successfully", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }

        profilePic?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
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

//        setFragmentResultListener("postActionResult") { _, _ ->
//            profileViewModel.fetchPosts()  // Fetch posts when returning from PostDetailsFragment
//        }



        return view

    }
    private fun loadedProfileImageInPickerHandler(result: ActivityResult){

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                filePath = getImageFilePath(selectedImageUri!!)
                Log.d("ProfileFragment", "loadedProfileImageInPickerHandler: ${filePath.toString()}")
                val sharedPrefHelper = SharedPrefHelper(requireContext())
                sharedPrefHelper.saveProfilePicUri(selectedImageUri.toString()
                )
                Log.d("ProfileFragment", "loadedProfileImageInPickerHandler: ${selectedImageUri.toString()}")
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(profilePic!!)

                val user = User(userId!!,email?.text.toString(),filePath.toString(),name?.text.toString(),lastUpdated)
                profileViewModel.updateProfile(user, true) { isUpdated ->
                    if (isUpdated) {
                        TrainHubApplication.Globals.currentUser?.profileImageUrl = filePath.toString()
                        Toast.makeText(context, "Profile image updated successfully", Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }

        profileViewModel.profilePic.observe(viewLifecycleOwner, Observer { newProfilePic ->
            Glide.with(this).load(newProfilePic).into(profilePic!!)
            postsAdapter.updateUserProfilePic(newProfilePic)
        })
    }
    private fun getImageFilePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.fetchPosts()
    }


}