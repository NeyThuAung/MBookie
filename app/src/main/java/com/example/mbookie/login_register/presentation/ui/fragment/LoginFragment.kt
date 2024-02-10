package com.example.mbookie.login_register.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.admin.ui.activity.AdminHomePageActivity
import com.example.mbookie.customer.ui.activity.CustomerHomePageActivity
import com.example.mbookie.databinding.FragmentLoginBinding
import com.example.mbookie.util.isValidEmail
import com.example.mbookie.util.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

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

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //login success
                        val userId = auth.currentUser?.uid.toString()

                        getUserRole(userId)

                    } else {
                        //login failed
                        requireContext().showToast(it.exception.toString())
                    }
                }

            }

        }

        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    private fun getUserRole(userId: String) {

        val ref = db.collection("users").document(userId)

        ref.get()
            .addOnSuccessListener {
                requireContext().showToast("Success.")

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
                requireContext().showToast(e.toString())
            }

    }

    private fun checkAllField(): Boolean {

        if (binding.etEmail.text.toString().isEmpty()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter email address."
            return false
        }

        if (!binding.etEmail.text.toString().isValidEmail()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter a valid email address."
            return false
        }

        if (binding.etPassword.text.toString().isEmpty()) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "This is require field."
            return false
        }

        if (binding.etPassword.text.toString().trim().length <= 6) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Password should at least 8 character long."
            return false
        }

        return true
    }

}