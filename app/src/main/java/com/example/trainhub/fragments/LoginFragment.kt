package com.example.trainhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.trainhub.R


class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        //setUpUI(view)
        return view
    }

    private fun setUpUI(view: View) {
        val emailEditText: EditText = view.findViewById(R.id.etLoginEmail)
        val passwordEditText: EditText = view.findViewById(R.id.etLoginPassword)
        val loginButton: Button = view.findViewById(R.id.btnLogin)
        val errorLoginTextView: TextView = view.findViewById(R.id.tvLoginError)

        loginButton.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(validate(email,password,errorLoginTextView)){
                //TODO check if email and password are in DB
            }
        }
    }

    private fun validate(email: String, password: String,error:TextView): Boolean {
        if (!email.contains("@")) {
            error.text = "Email must contain @"
            return false
        }

        if (password.length <= 6) {
            error.text = "Password must be more than 6 characters"
            return false
        }
        return true
    }

    fun signUp(view: View) {
        //TODO change to register fragment
    }


}