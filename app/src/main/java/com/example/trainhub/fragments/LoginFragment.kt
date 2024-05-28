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


class LoginFragment : Fragment() {
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? =null
    private var loginButton: Button? = null
    private var errorLoginTextView: TextView? = null
    private var signUpTextView:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        setUpUI(view)
        return view
    }

    private fun setUpUI(view: View) {
        emailEditText = view.findViewById(R.id.etRegisterEmail)
        passwordEditText = view.findViewById(R.id.etRegisterPassword)
        loginButton = view.findViewById(R.id.btnRegister)
        errorLoginTextView = view.findViewById(R.id.tvRegisterError)
        signUpTextView = view.findViewById(R.id.tvLoginSignUp)
        val action = Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signUpFragment)
        signUpTextView?.setOnClickListener(action)

        loginButton?.setOnClickListener{
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            if(validate(email,password,errorLoginTextView)){
                //TODO check if email and password are in DB
            }
        }
    }

    private fun validate(email: String, password: String,error:TextView?): Boolean {
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