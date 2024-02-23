package com.example.mbookie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.mbookie.admin.ui.activity.AdminHomePageActivity
import com.example.mbookie.customer.ui.activity.CustomerHomePageActivity
import com.example.mbookie.databinding.ActivityMainBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.example.mbookie.util.FireStoreTables
import com.example.mbookie.util.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    private val db = Firebase.firestore

    private var adminOrCustomer = "0" //0 is admin //1 is customer
    private lateinit var userTable : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.primary_black)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

//        Handler(Looper.getMainLooper()).postDelayed(
//            {
//                val currentUser = auth.currentUser
//
//                if (currentUser != null){
//                    val userId = auth.currentUser?.uid.toString()
//                    getUserRole(userId)
//                }else{
//                    val intent = Intent(applicationContext,LoginRegisterActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//
//            },1000
//        )
        val currentUser = auth.currentUser

        if (currentUser != null){
            val userId = auth.currentUser?.uid.toString()
            getUserRole(userId)
        }else{
            val intent = Intent(applicationContext,LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // get user role to specify admin or customer
    private fun getUserRole(userId: String) {

        if (adminOrCustomer == "0"){
            Log.d("JGKHKJH", "getUserRole: $adminOrCustomer")
            userTable = FireStoreTables.ADMIN
        }else{
            userTable = FireStoreTables.CUSTOMER
        }

        val ref = db.collection(userTable).document(userId)

        ref.get()
            .addOnSuccessListener {
                showToast("Success.")
                Log.d("HKJHKJ", "getUserRole: ${it.data?.get("isAdmin").toString()}")
                if (it != null){
                    val isAdmin = it.data?.get("isAdmin").toString()

                    if (isAdmin.toInt() == 0){
                        val intent = Intent(this, AdminHomePageActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, CustomerHomePageActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }

            }
            .addOnFailureListener {e->
                showToast(e.toString())
            }
    }
}