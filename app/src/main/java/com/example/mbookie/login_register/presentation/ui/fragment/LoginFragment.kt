package com.example.mbookie.login_register.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.admin.ui.activity.AdminHomePageActivity
import com.example.mbookie.customer.ui.activity.CustomerHomePageActivity
import com.example.mbookie.databinding.FragmentLoginBinding
import com.example.mbookie.util.FireStoreTables
import com.example.mbookie.util.isValidEmail
import com.example.mbookie.util.showToast
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.firestore


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private var adminOrCustomer = "0" //0 is admin //1 is customer
    private lateinit var userTable : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setUpTextChangeListeners()
        onClickEvents()

        binding.etEmail.doAfterTextChanged {
            if (it.toString().isNotEmpty() && it.toString().isValidEmail()) {
                binding.tilEmail.isErrorEnabled = false
            }
        }

    }

    private fun onClickEvents() {

        binding.llRegisterHere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.mbLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (checkAllField()) {
                binding.mbLogin.text = null
                binding.progressLoad.isVisible = true
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->
                    if (task.isSuccessful) {
                        //login success
                        val userId = auth.currentUser?.uid.toString()

                        getUserRole(userId)

                    } else {
                        //login failed
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException) {
                            requireActivity().showToast("Email is not registered.")
                        } else if (exception is FirebaseAuthInvalidCredentialsException) {
                            requireActivity().showToast("Incorrect email or password.")
                        } else {
                            requireActivity().showToast("Authentication failed")
                        }

                        binding.mbLogin.text = "Login"
                        binding.progressLoad.isVisible = false
                    }
                }

            }

        }

        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    private fun getUserRole(userId: String) {

        if (adminOrCustomer == "0" || binding.etEmail.text.toString().contains("admin")){
            userTable = FireStoreTables.ADMIN
        }else{
            userTable = FireStoreTables.CUSTOMER
        }

        val ref = db.collection(userTable).document(userId)

        ref.get()
            .addOnSuccessListener {
                binding.mbLogin.text = "Login"
                binding.progressLoad.isVisible = false
                if (it != null){
                    val isAdmin = it.data?.get("isAdmin").toString()

                    if (isAdmin.toInt() == 0){
                        val intent = Intent(requireContext(), AdminHomePageActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(requireContext(), CustomerHomePageActivity::class.java)
                        startActivity(intent)
                    }
                    requireActivity().finish()
                }

            }
            .addOnFailureListener {e->
                binding.mbLogin.text = "Login"
                binding.progressLoad.isVisible = false
                requireContext().showToast(e.toString())
            }

    }

    private fun checkAllField(): Boolean {

        var isValid = true  // Assume all fields are valid initially

        if (binding.etEmail.text.toString().isEmpty()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter email address."
            isValid = false  // Email is not valid, set isValid to false
        } else if (!binding.etEmail.text.toString().isValidEmail()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter a valid email address."
            isValid = false  // Email is not valid, set isValid to false
        } else {
            binding.tilEmail.isErrorEnabled = false  // Hide error if email is valid
        }

        if (binding.etPassword.text.toString().isEmpty()) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "This is required field."
            isValid = false  // Password is not valid, set isValid to false
        } else if (binding.etPassword.text.toString().trim().length <= 6) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Password should be at least 8 characters long."
            isValid = false  // Password is not valid, set isValid to false
        } else if (!isStrongPassword(binding.etPassword.text.toString())) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Password should contain A-Z, a-z, 0-9,\n" +
                    "Special characters (e.g., !, @, #, \$, %, ^, &, *, etc.)"
            isValid = false  // Password is not strong, set isValid to false
        } else {
            binding.tilPassword.isErrorEnabled = false  // Hide error if password is valid
        }
        binding.mbLogin.checkRequirement()
        return isValid
    }

    private fun setUpTextChangeListeners() {
        binding.etEmail.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                binding.tilEmail.error = "Enter email address."
            } else if (!it.toString().isValidEmail()) {
                binding.tilEmail.error = "Enter a valid email address."
            } else {
                binding.tilEmail.error = null // Clear error if valid
            }

            binding.mbLogin.checkRequirement()
        }

        binding.etPassword.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                binding.tilPassword.error = "This is required field."
            } else if (it.toString().trim().length <= 6) {
                binding.tilPassword.error = "Password should be at least 8 characters long."
            } else if (!isStrongPassword(it.toString())) {
                binding.tilPassword.error = "Password should contain A-Z, a-z, 0-9,\nSpecial characters (e.g., !, @, #, \$, %, ^, &, *, etc.)"
            } else {
                binding.tilPassword.error = null // Clear error if valid
            }
            binding.mbLogin.checkRequirement()
        }

    }

    private fun MaterialButton.checkRequirement(){
        isEnabled = binding.tilEmail.error.isNullOrEmpty() && binding.tilPassword.error.isNullOrEmpty()
    }

    private fun isStrongPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }

}