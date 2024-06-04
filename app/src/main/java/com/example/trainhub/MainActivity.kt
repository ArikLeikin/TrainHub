package com.example.trainhub

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.trainhub.fragments.AddPostFragment
import com.example.trainhub.fragments.DiscoverFragment
import com.example.trainhub.fragments.HomeFragment
import com.example.trainhub.fragments.LocationFragment
import com.example.trainhub.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null
    private val homeFragment = HomeFragment()
    private var bottomNavigationView:BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.let {
            Navigation.setViewNavController(it, navController)
        }


        val discoverFragment = DiscoverFragment()
        val locationFragment = LocationFragment()
        val profileFragment = ProfileFragment()

        bottomNavigationView?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.icHome -> replaceFragment(homeFragment)
                R.id.icDiscover -> replaceFragment(discoverFragment)
                R.id.icAddPost -> replaceFragment(AddPostFragment())
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

