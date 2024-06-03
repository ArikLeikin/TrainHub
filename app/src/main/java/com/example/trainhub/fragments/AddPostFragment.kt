package com.example.trainhub.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.entities.Post
import com.example.trainhub.viewModel.AddPostViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddPostFragment : Fragment() {

    var postImage: ImageView? = null
    var postTitle: EditText? = null
    var description: EditText? = null
    var postLocation:String? = null
    var postImageUrl:String? = null
    var addPostButton: Button? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var filePath: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val AddPostViewModel = AddPostViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)

        setUpUI(view)
        return view
    }

    private fun setUpUI(view: View) {
        postImage = view.findViewById(R.id.ivAddPostImage)
        postTitle = view.findViewById(R.id.etAddPostTitle)
        description = view.findViewById(R.id.etAddPostDescription)
        addPostButton = view.findViewById(R.id.btnAddPost)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){loadedProfileImageInPickerHandler(it)}

        postImage?.setOnClickListener {
            openImagePicker()
        }

        addPostButton?.setOnClickListener{
            getLocation(){locationIsSet->
                if(locationIsSet){
                    //add post
                    println(postLocation)

                    val post = Post("",postTitle?.text.toString(), description?.text.toString(),"", TrainHubApplication.Globals.currentUser!!.id,"asdasdqwe",postLocation!!)

                    post.imageUrl = selectedImageUri.toString()
                    AddPostViewModel.addPost(post){
                        if(it){
                            Toast.makeText(context, "Post added successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Failed to add post", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
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

    private fun getLocation(callback:(Boolean)->Unit) {
        if(checkLocationPermission()){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                .addOnCompleteListener{task->
                    if (task.isSuccessful && task.result != null) {
                        val location: Location = task.result
                        val latitude = location.latitude
                        val longitude = location.longitude
                        postLocation = "$latitude,$longitude"
                        // Use the latitude and longitude as needed
                        Toast.makeText(context, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                        callback(true)
                    } else {
                        Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                }
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            callback(false)
        }
    }

    private fun checkLocationPermission(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }


}