package com.example.mbookie.login_register.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentAlreadyEmailSentBinding


class AlreadyEmailSentFragment : Fragment() {

    private lateinit var binding : FragmentAlreadyEmailSentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlreadyEmailSentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mbOkay.setOnClickListener {
            findNavController().navigate(R.id.action_alreadyEmailSentFragment_to_loginFragment)
        }
    }

}