package com.example.trainhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.trainhub.R

class SignUpFragment : Fragment() {

    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var errorLoginTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    private fun setUpUi(view: View){
        // Initialize EditText fields
        emailEditText = view.findViewById(R.id.etRegisterEmail)
        passwordEditText = view.findViewById(R.id.etRegisterPassword)
        errorLoginTextView = view.findViewById(R.id.tvRegisterError)

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            // Your login/signup logic here
            if(validate(email,password,errorLoginTextView)){
                //TODO check if email and password are in DB
            }
            Navigation.findNavController(view).navigate(R.id.loginFragment)
        }
    }

    private fun validate(email: String, password: String,error: TextView?): Boolean {
        if (!email.contains("@")) {
            error?.text = "Email must contain @"
            return false
        }

        if (password.length <= 6) {
            error?.text = "Password must be more than 6 characters"
            return false
        }
        return true
    }

}
