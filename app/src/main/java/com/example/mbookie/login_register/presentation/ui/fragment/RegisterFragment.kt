package com.example.mbookie.login_register.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mbookie.databinding.FragmentRegisterBinding
import com.example.mbookie.util.isValidEmail
import com.example.mbookie.util.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth : FirebaseAuth

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

        onClickEvents()

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
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }

    //save user info to database table
    private fun saveUserInfo(userId: String) {

        val db = Firebase.firestore

        val user = hashMapOf(
            "userId" to userId,
            "email" to email,
            "userName" to userName,
            "password" to password,
            "phone" to "",
            "birthday" to "",
            "gender" to "",
            "profileUrl" to "",
            "isAdmin" to "1" //0 is admin // 1 is customer
        )

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener { _ ->
                requireContext().showToast("Successfully Added.")

                findNavController().popBackStack()

            }
            .addOnFailureListener { e ->
                requireContext().showToast(e.toString())
            }
    }

    private fun checkAllField() : Boolean{

        if (binding.etEmail.text.toString().isEmpty()){
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter email address."
            return false
        }

        if (!binding.etEmail.text.toString().isValidEmail()){
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter a valid email address."
            return false
        }

        if (binding.etPassword.text.toString().isEmpty()){
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "This is require field."
            return false
        }

        if (binding.etPassword.text.toString().trim().length <= 6){
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Password should at least 8 character long."
            return false
        }

        if (binding.etConfirmPassword.text.toString().isEmpty()){
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = "This is require field."
            return false
        }

        if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()){
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = "Password is not match."
            return false
        }

        return true
    }

}