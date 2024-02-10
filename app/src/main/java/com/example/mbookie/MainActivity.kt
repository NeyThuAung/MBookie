package com.example.mbookie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.mbookie.admin.ui.activity.AdminHomePageActivity
import com.example.mbookie.customer.ui.activity.CustomerHomePageActivity
import com.example.mbookie.databinding.ActivityMainBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.example.mbookie.util.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.primary_black)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val currentUser = auth.currentUser

                if (currentUser != null){
                    val userId = auth.currentUser?.uid.toString()
                    getUserRole(userId)
                }else{
                    val intent = Intent(applicationContext,LoginRegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            },1000
        )

    }

    // get user role to specify admin or customer
    private fun getUserRole(userId: String) {

        val ref = db.collection("users").document(userId)

        ref.get()
            .addOnSuccessListener {
                this.showToast("Success.")

                if (it != null){
                    val isAdmin = it.data?.get("isAdmin").toString()

                    if (isAdmin.toInt() == 0){
                        val intent = Intent(this, AdminHomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(this, CustomerHomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }

            }
            .addOnFailureListener {e->
                this.showToast(e.toString())
            }
    }
}