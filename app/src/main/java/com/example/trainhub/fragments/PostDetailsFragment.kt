package com.example.trainhub.fragments

import android.app.Activity
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User
import com.example.trainhub.viewModel.PostDetailsViewModel
import com.google.android.material.button.MaterialButton


class PostDetailsFragment : Fragment() {

    private var postImage:ImageView? = null
    private var profileImage:ImageFilterView? = null
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

        postDetailsViewModel = ViewModelProvider(this)[PostDetailsViewModel::class.java]

        postDetailsViewModel.post.observe(viewLifecycleOwner){
            post = it
            postTitle!!.setText(post?.title)
            postDescription!!.setText(post?.description)

            Glide.with(requireContext())
                .load(Uri.parse(post?.imageUrl))
                .into(postImage!!)

        }
        postDetailsViewModel.user.observe(viewLifecycleOwner){
            user = it
            Glide.with(requireContext())
                .load(Uri.parse(user?.profileImageUrl))
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
        }

        arguments?.getString("postId")?.let {postId->
            postDetailsViewModel.fetchPost(postId)

        }

        deleteBtn?.setOnClickListener{
            postDetailsViewModel.deletePost(post!!)
        }

        postDetailsViewModel.saveStatus.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(context, "Post saved changes!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Post not saved!", Toast.LENGTH_SHORT).show()
            }
        }

        postDetailsViewModel.deleteStatus.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigateUp()
            }else{
                Toast.makeText(context, "Error! Post not deleted!", Toast.LENGTH_SHORT).show()
            }
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){loadedProfileImageInPickerHandler(it)}

        editBtn?.setOnClickListener(){
            println(post!!)
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