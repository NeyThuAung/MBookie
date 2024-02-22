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
import com.example.mbookie.admin.ui.adapter.MovieListAdapter
import com.example.mbookie.admin.ui.adapter.SelectCinemaAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.FragmentSelectCinemaBinding
import com.example.mbookie.databinding.FragmentSelectGenreBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SelectCinemaFragment : Fragment(),SelectCinemaAdapter.OnItemClickListener {

    private lateinit var binding : FragmentSelectCinemaBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private lateinit var selectCinemaAdapter : SelectCinemaAdapter
    private var selectedCinemaId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectCinemaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        onClickEvents()

        movieViewModel.getCinemaList()
        movieViewModel.cinemaList.observe(viewLifecycleOwner) { state ->
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
                    Log.d("KHLKJHLJ", "onViewCreated: ${state.data}")
                    selectCinemaAdapter = SelectCinemaAdapter(state.data as ArrayList<Cinema>,this)
                    binding.recSelectCinemaList.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = selectCinemaAdapter
                    }

                }
            }
        }

    }

    private fun onClickEvents() {

        binding.mbManageCinema.setOnClickListener {
            findNavController().navigate(R.id.action_selectCinemaFragment_to_manageCinemaFragment)
        }

        binding.tvSave.setOnClickListener {
            findNavController().navigate(R.id.action_selectCinemaFragment_to_cinemaShowTimesFragment)
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onMovieRootClick(cinema : Cinema) {
        movieViewModel.selectedCinema = cinema
        binding.tvSave.isEnabled = true
    }


}