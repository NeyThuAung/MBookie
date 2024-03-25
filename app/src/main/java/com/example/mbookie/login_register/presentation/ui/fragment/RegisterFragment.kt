package com.example.mbookie.login_register.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.mbookie.databinding.FragmentRegisterBinding
import com.example.mbookie.util.FireStoreTables
import com.example.mbookie.util.isValidEmail
import com.example.mbookie.util.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore


    private lateinit var email : String
    private lateinit var userName : String
    private lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        setUpTextChangeListeners()
        onClickEvents()

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
        }

        binding.etUserName.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                binding.tilUserName.error = "Enter username."
            }else {
                binding.tilUserName.error = null // Clear error if valid
            }
        }

        binding.etPassword.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                binding.tilPassword.error = "This is required field."
            } else if (it.toString().trim().length <= 6) {
                binding.tilPassword.error = "Password should be at least 8 characters long."
            } else if (!isStrongPassword(it.toString())) {
                binding.tilPassword.error = "Password should contain A-Z, a-z, 0-9,\nSpecial characters (e.g., !, @, #, \$, %, ^, &, *, etc.)"
            } else {
                binding.tilPassword.isErrorEnabled = false
                binding.tilPassword.error = null // Clear error if valid
            }
        }

        binding.etConfirmPassword.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                binding.tilConfirmPassword.error = "This is required field."
            } else if (it.toString() != binding.etPassword.text.toString()) {
                binding.tilConfirmPassword.error = "Password does not match."
            } else {
                binding.tilConfirmPassword.isErrorEnabled = false
                binding.tilConfirmPassword.error = null // Clear error if valid
            }
        }
    }


    private fun onClickEvents(){

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.llAlreadyAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.mbRegister.setOnClickListener {
            if (checkAllField()){

                binding.mbRegister.text = null
                binding.progressLoad.isVisible = true
                email = binding.etEmail.text.toString()
                userName = binding.etUserName.text.toString()
                password = binding.etConfirmPassword.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            val userId = auth.currentUser?.uid.toString()
                            saveUserInfo(userId)

                        }
                    }
                    .addOnFailureListener {
                        binding.mbRegister.text = "Register"
                        binding.progressLoad.isVisible = false
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }

    //save user info to database table
    private fun  saveUserInfo(userId : String) {

        val admin = hashMapOf(
            "userId" to userId,
            "email" to email,
            "userName" to userName,
            "password" to password,
            "phone" to "",
            "birthday" to "",
            "gender" to "",
            "profileUrl" to "",
            "userType" to "customer"
        )

        db.collection(FireStoreTables.USER)
            .document(userId)
            .set(admin)
            .addOnSuccessListener { _ ->
                binding.mbRegister.text = "Register"
                binding.progressLoad.isVisible = false
                requireContext().showToast("Successfully registered.")

                findNavController().popBackStack()

            }
            .addOnFailureListener { e ->
                binding.mbRegister.text = "Register"
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

        if (binding.etUserName.text.toString().isEmpty()) {
            binding.tilUserName.isErrorEnabled = true
            binding.tilUserName.error = "Enter username."
            isValid = false  // Email is not valid, set isValid to false
        }else {
            binding.tilUserName.isErrorEnabled = false  // Hide error if email is valid
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

        if (binding.etConfirmPassword.text.toString().isEmpty()) {
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = "This is required field."
            isValid = false  // Confirm password is not valid, set isValid to false
        } else if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = "Password does not match."
            isValid = false  // Confirm password is not valid, set isValid to false
        } else {
            binding.tilConfirmPassword.isErrorEnabled = false  // Hide error if confirm password is valid
        }

        return isValid
    }

    fun isStrongPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }




}