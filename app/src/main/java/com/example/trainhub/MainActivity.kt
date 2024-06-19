package com.example.trainhub

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.trainhub.fragments.AddPostFragment
import com.example.trainhub.fragments.DiscoverFragment
import com.example.trainhub.fragments.EditProfileFragment
import com.example.trainhub.fragments.HomeFragment
import com.example.trainhub.fragments.LocationFragment
import com.example.trainhub.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private val homeFragment = HomeFragment()
    private val discoverFragment = DiscoverFragment()
    private val locationFragment = LocationFragment()
    private val profileFragment = ProfileFragment()
    private val addPostFragment = AddPostFragment()
    private val editProfileFragment = EditProfileFragment()
    private var bottomNavigationView:BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.let {
            Navigation.setViewNavController(it, navController)
        }



        bottomNavigationView?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.icHome -> replaceFragment(homeFragment)
                R.id.icDiscover -> replaceFragment(discoverFragment)
                R.id.icAddPost -> replaceFragment(addPostFragment)
                R.id.icLocation -> replaceFragment(locationFragment)
                R.id.icProfile -> replaceFragment(profileFragment)
            }
            true
        }


        navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener { _, destination, _  ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> bottomNavigationView?.visibility = View.GONE
                else -> bottomNavigationView?.visibility = View.VISIBLE

            }
        }

//       bottomNavigationView?.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//
//                R.id.icHome -> {
//
//                    navController?.navigate(R.id.homeFragment)
//                    true
//                }
//                R.id.icDiscover -> {
//
//                    navController?.navigate(R.id.discoverFragment)
//                    true
//                }
//                R.id.icAddPost -> {
//                    navController?.navigate(R.id.addPostFragment)
//                    true
//                }
//                R.id.icLocation -> {
//                    navController?.navigate(R.id.locationFragment)
//                    true
//                }
//                R.id.icProfile -> {
//                    navController?.navigate(R.id.profileFragment)
//                    true
//                }
//                else -> false
//
//            }
//        }

    }
    fun moveToEditProfile(){
        val activity = this
        activity.replaceFragment(editProfileFragment)
    }

    fun moveToHome(){
        bottomNavigationView?.selectedItemId = R.id.icHome
        replaceFragment(homeFragment)
    }

    fun replaceFragment(fragment: Fragment){
        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.navHostMain,fragment)
            transaction.commit()
        }

    }



}

