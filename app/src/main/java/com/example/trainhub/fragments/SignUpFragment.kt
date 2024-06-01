package com.example.trainhub.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.trainhub.R

class SignUpFragment : Fragment() {
    private var nicknameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var registerButton: Button? = null
    private var profileImgView: ImageView? = null
    private var backToLoginText: TextView? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        setUpUi(view)

        return view
    }

    private fun setUpUi(view: View){
        nicknameEditText = view.findViewById(R.id.etNickname)
        profileImgView = view.findViewById(R.id.ivProfileImage)
        profileImgView?.setOnClickListener {
            openImagePicker()
            Toast.makeText(context,"Choose an Image", Toast.LENGTH_SHORT).show()
            Log.i(TAG,"change profileImageButton clicked")
        }
        emailEditText = view.findViewById(R.id.etRegisterEmail)
        passwordEditText = view.findViewById(R.id.etRegisterPassword)
        registerButton = view.findViewById(R.id.btnRegister)
        registerButton?.setOnClickListener {
            val nickname = nicknameEditText?.text.toString()
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            if(validate(nickname,email,password)){
                Navigation.findNavController(view).navigate(R.id.loginFragment)
            }
        }
        backToLoginText = view.findViewById(R.id.tvBackToLogin)
        backToLoginText?.setOnClickListener{
            findNavController().navigateUp()
        }

        selectedImageUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.user_profile}")
        Glide.with(this)
            .load(selectedImageUri)
            .into(profileImgView!!)
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){loadedProfileImageInPickerHandler(it)}
    }

    private fun loadedProfileImageInPickerHandler(result: ActivityResult){
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(profileImgView!!)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun validate(nickname: String, email: String, password: String): Boolean {
        if (nickname.length < 3){
            Toast.makeText(context,"Nickname must contain at least 3 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.contains("@")) {
            Toast.makeText(context,"Email must contain @", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(context,"Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}
