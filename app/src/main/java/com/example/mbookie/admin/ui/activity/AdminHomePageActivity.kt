package com.example.mbookie.admin.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mbookie.R
import com.example.mbookie.databinding.ActivityAdminHomePageBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminHomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomePageBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.primary_black)
        binding = ActivityAdminHomePageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.fragment_container)
        navView.itemIconTintList = null
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dashBoardFragment -> {
                    navController.navigate(R.id.dashBoardFragment)
                }
                R.id.movieListFragment -> {
                    navController.navigate(R.id.movieListFragment)
                }
                R.id.bookingListFragment -> {
                    navController.navigate(R.id.bookingListFragment)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                }

            }
            return@setOnItemSelectedListener true
        }

    }
}