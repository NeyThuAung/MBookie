package com.example.mbookie.customer.ui.activity

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
import com.example.mbookie.databinding.ActivityCustomerHomePageBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerHomePageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCustomerHomePageBinding

    private lateinit var auth : FirebaseAuth

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.primary_black)

        binding = ActivityCustomerHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.fragment_container)
        navView.itemIconTintList = null
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.customerHomeFragment -> {
                    navController.navigate(R.id.customerHomeFragment)
                }
                R.id.customerBookingListFragment -> {
                    navController.navigate(R.id.customerBookingListFragment)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                }

            }
            return@setOnItemSelectedListener true
        }

//        binding.tvSignOut.setOnClickListener{
//            auth.signOut()
//            val intent = Intent(this, LoginRegisterActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
}