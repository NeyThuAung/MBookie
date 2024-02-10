package com.example.mbookie.login_register.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentForgetPasswordBinding
import com.example.mbookie.util.isValidEmail
import com.example.mbookie.util.showToast
import com.google.firebase.auth.FirebaseAuth


class ForgetPasswordFragment : Fragment() {

    private lateinit var binding : FragmentForgetPasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mbSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString()
            auth = FirebaseAuth.getInstance()

            if (checkEmailFormat()){
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful){

                        requireContext().showToast("Email sent!")
                        findNavController().navigate(R.id.action_forgetPasswordFragment_to_alreadyEmailSentFragment)

                    }else{

                        requireContext().showToast(it.exception.toString())

                    }
                }
            }

        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun checkEmailFormat() : Boolean{

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


        return true
    }


}