package com.example.mbookie.customer.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.mbookie.R
import com.example.mbookie.databinding.ActivityCustomerHomePageBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.google.firebase.auth.FirebaseAuth

class CustomerHomePageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCustomerHomePageBinding

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.primary_black)


        binding = ActivityCustomerHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tvSignOut.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}