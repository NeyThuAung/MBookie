package com.example.mbookie.customer.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentCustomerBookingListBinding
import com.example.mbookie.login_register.presentation.ui.activity.LoginRegisterActivity
import com.google.firebase.auth.FirebaseAuth


class CustomerBookingListFragment : Fragment() {

    private lateinit var binding : FragmentCustomerBookingListBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCustomerBookingListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.tvSignOut.setOnClickListener{
            auth.signOut()
            val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
            startActivity(intent)
        }

    }

}