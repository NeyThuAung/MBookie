package com.example.mbookie.admin.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.AvailableCinemaAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Show
import com.example.mbookie.databinding.FragmentAvailableCinemaBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class AvailableCinemaFragment : Fragment(),AvailableCinemaAdapter.OnItemClickListener {

    private lateinit var binding : FragmentAvailableCinemaBinding

    private lateinit var availableCinemaAdapter: AvailableCinemaAdapter

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
        binding = FragmentAvailableCinemaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        onClickEvent()

        movieViewModel.getCinemaListForMovie(
            movieViewModel.movieId
        )
        movieViewModel.cinemaListForMovie.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    Toast.makeText(requireContext(), state.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    loadingDialog.hideDialog()

                    if (state.data.isNullOrEmpty()) {
                        binding.tvSelectedCinemaTitle.isVisible = false
                        binding.recSelectedCinemaForMovie.isVisible = false
                    } else {
                        binding.tvSelectedCinemaTitle.isVisible = true
                        binding.recSelectedCinemaForMovie.isVisible = true

                        availableCinemaAdapter = AvailableCinemaAdapter(state.data as ArrayList<Cinema>,this)
                        binding.recSelectedCinemaForMovie.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                            adapter = availableCinemaAdapter
                        }
                    }
                }
            }
        }

    }

    private fun onClickEvent() {
        binding.cvAddCinema.setOnClickListener{
            findNavController().navigate(R.id.action_availableCinemaFragment_to_selectCinemaFragment)
        }

        binding.tvSave.setOnClickListener {
            findNavController().navigate(R.id.action_availableCinemaFragment_to_movieListFragment)
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onMovieRootClick(cinema: Cinema) {
        movieViewModel.selectedCinema = cinema
        findNavController().navigate(R.id.action_availableCinemaFragment_to_cinemaShowTimesFragment)
    }
}