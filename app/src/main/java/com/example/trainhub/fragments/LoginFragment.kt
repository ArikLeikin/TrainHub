package com.example.trainhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.trainhub.R
import com.example.trainhub.viewModel.SignInViewmodel


class LoginFragment : Fragment() {
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? =null
    private var signInButton: Button? = null
    private var signUpTextView:TextView? = null
    private var vm:SignInViewmodel? = null
    private var pb:ProgressBar? = null

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
        vm = SignInViewmodel()
        vm?.signInStatus?.observe(viewLifecycleOwner) { isSuccess ->
            pb?.visibility = View.GONE
            if (isSuccess) {
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.homeFragment)
            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun setUpUI(view: View) {
        emailEditText = view.findViewById(R.id.etSignInEmail)
        passwordEditText = view.findViewById(R.id.etSignInPassword)
        signInButton = view.findViewById(R.id.btnSignIn)
        signUpTextView = view.findViewById(R.id.tvSignUp)
        val action = Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signUpFragment)
        signUpTextView?.setOnClickListener(action)
        pb = view.findViewById(R.id.pbSignIn)

        signInButton?.setOnClickListener{
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            if(validate(email,password)){
                vm?.signIn(email,password)
            }
        }
    }

    private fun signIn(email: String, password: String){
        vm?.signIn(email,password)
    }

    private fun validate(email: String, password: String): Boolean {
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