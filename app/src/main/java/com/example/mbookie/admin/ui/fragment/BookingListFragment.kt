package com.example.mbookie.admin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mbookie.R
import com.example.mbookie.databinding.FragmentBookingListBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.viewmodel.MovieViewModel

class BookingListFragment : Fragment() {

    private lateinit var binding : FragmentBookingListBinding
    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookingListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}