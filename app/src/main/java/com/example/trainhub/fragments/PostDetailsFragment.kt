package com.example.trainhub.fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.trainhub.R
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User
import com.example.trainhub.viewModel.PostDetailsViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage


class PostDetailsFragment : Fragment() {

    private var postImage:ImageView? = null
    private var profileImage:ImageView? = null
    private var postTitle:EditText? = null
    private var postDescription:EditText? = null
    private var postUserName:TextView? = null
    private var editBtn:MaterialButton? = null
    private var deleteBtn:MaterialButton? = null
    private var post: Post? = null
    private var user : User? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var filePath: String? = null
    private var imageChanged = false
    private var progressBar: ProgressBar? = null

    private lateinit var postDetailsViewModel : PostDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        setUpUI(view)
        return view
    }

    private fun setUpUI(view: View) {
        postImage = view.findViewById(R.id.ivPostDetailsImage)
        postTitle = view.findViewById(R.id.tvPostDetailsTitle)
        postDescription = view.findViewById(R.id.tvPostDetailsDescription)
        postUserName = view.findViewById(R.id.tvPostDetailsName)
        editBtn = view.findViewById(R.id.mbPostDetailsEdit)
        deleteBtn = view.findViewById(R.id.mbPostDetailsDelete)
        profileImage = view.findViewById(R.id.ifvPostDetailsProfileImg)
        progressBar = view.findViewById(R.id.pbPostDetails)

        postDetailsViewModel = ViewModelProvider(this)[PostDetailsViewModel::class.java]

        postDetailsViewModel.post.observe(viewLifecycleOwner){
            post = it
            postTitle!!.setText(post?.title)
            postDescription!!.setText(post?.description)
//            val uri = Uri.parse(post?.imageUrl)
            Glide.with(requireContext())
                .load(post?.imageUrl)
                .into(postImage!!)

//            val theImage = GlideUrl(
//                post?.imageUrl, LazyHeaders.Builder()
//                    .addHeader("User-Agent", "5")
//                    .build()
//            )


        }
        postDetailsViewModel.user.observe(viewLifecycleOwner){
            user = it
            println(user!!.profileImageUrl)
//            Firebase.storage.reference.child("profile_images/${user!!.profileImageUrl}").downloadUrl.addOnSuccessListener { uri ->
//                println(uri)
//            }

            Glide.with(requireContext())
                .load(user!!.profileImageUrl)
                .into(profileImage!!)


            postUserName!!.text = user?.name
            if(!this.post?.userId.equals(user?.id)){
                editBtn?.visibility = View.GONE
                deleteBtn?.visibility = View.GONE
                postTitle?.isEnabled = false
                postDescription?.isEnabled = false
            }else{
                postImage!!.setOnClickListener {
                    openImagePicker()
                }
                selectedImageUri = Uri.parse(post?.imageUrl)
            }
            progressBar?.visibility = View.GONE
        }

        arguments?.getString("postId")?.let {postId->
            progressBar?.visibility = View.VISIBLE
            postDetailsViewModel.fetchPost(postId)

        }

        deleteBtn?.setOnClickListener{
            progressBar?.visibility = View.VISIBLE
            postDetailsViewModel.deletePost(post!!)
        }

        postDetailsViewModel.saveStatus.observe(viewLifecycleOwner){
            progressBar?.visibility = View.GONE
            if(it){
                Toast.makeText(context, "Post saved changes!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Post not saved!", Toast.LENGTH_SHORT).show()
            }
        }

        postDetailsViewModel.deleteStatus.observe(viewLifecycleOwner){
            progressBar?.visibility = View.GONE
            if(it){
                findNavController().navigateUp()
            }else{
                Toast.makeText(context, "Error! Post not deleted!", Toast.LENGTH_SHORT).show()
            }
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){loadedProfileImageInPickerHandler(it)}

        editBtn?.setOnClickListener(){
            progressBar?.visibility = View.VISIBLE
            post!!.title = postTitle?.text.toString()
            post!!.description = postDescription?.text.toString()
            post!!.imageUrl = selectedImageUri.toString()
            postDetailsViewModel.updatePost(post!!,imageChanged)
        }
    }

    private fun loadedProfileImageInPickerHandler(result: ActivityResult){
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                filePath = getImageFilePath(selectedImageUri!!)
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(postImage!!)
                imageChanged = true
            }
        }
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

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }


}