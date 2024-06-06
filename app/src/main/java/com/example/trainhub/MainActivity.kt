package com.example.trainhub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.let {
            Navigation.setViewNavController(it, navController)
        }
        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _  ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE

            }
        }
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.HomeFragment -> {
                    navController?.navigate(R.id.HomeFragment)
                    true
                }
                R.id.DiscoverFragment -> {
                    navController?.navigate(R.id.discoverFragment)
                    true
                }
                R.id.addPostFragment -> {
                    navController?.navigate(R.id.AddPostFragment)
                    true
                }
                R.id.LocationFragment -> {
                    navController?.navigate(R.id.LocationFragment)
                    true
                }
                R.id.ProfileFragment -> {
                    navController?.navigate(R.id.ProfileFragment)
                    true
                }
                else -> false
            }
        }


    }



}

