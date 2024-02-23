package com.example.mbookie.admin.ui.fragment

import android.annotation.SuppressLint
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
import com.example.mbookie.admin.ui.adapter.GenreAdapter
import com.example.mbookie.admin.ui.adapter.ManageCinemaAdapter
import com.example.mbookie.admin.ui.adapter.MovieListAdapter
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.FragmentMovieListBinding
import com.example.mbookie.util.DeleteDialog
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MovieListFragment : Fragment(), MovieListAdapter.OnItemClickListener,DeleteDialog.AlertButtonListener {

    private lateinit var binding: FragmentMovieListBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private lateinit var movieListAdapter: MovieListAdapter

    private val deleteDialog: DeleteDialog by lazy {
        DeleteDialog(requireActivity())
    }

    private var deleteMovieId = ""
    private var removeMoviePos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form  Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = true

        binding.floatingActionAddMovie.setOnClickListener {
            findNavController().navigate(R.id.action_movieListFragment_to_addMovieFragment)
        }

        movieViewModel.getMovieList()
        movieViewModel.movieList.observe(viewLifecycleOwner) { state ->
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

                    movieViewModel.movieDetailList.clear()
                    movieViewModel.movieDetailList.addAll(state.data as ArrayList<MovieDetail>)
                    initRecycler()

                }
            }
        }

    }

    private fun initRecycler() {
        movieListAdapter = MovieListAdapter(movieViewModel.movieDetailList, this)
        binding.recMovieList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = movieListAdapter
        }
    }

    override fun onEditMovieClick(movieDetail: MovieDetail) {
        movieViewModel.movieId = movieDetail.mId.toString()
        movieViewModel.editMovieDetail = movieDetail
        findNavController().navigate(R.id.action_movieListFragment_to_addMovieFragment)
    }

    override fun onDeleteMovieClick(movieName : String, movieId: String, removePos: Int) {
        deleteMovieId = movieId
        removeMoviePos = removePos
        deleteDialog.showDialog("Are you sure?",this,"Are you sure to delete this \"$movieName\" movie?")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPositiveClick() {
        movieViewModel.deleteMovie(
            deleteMovieId
        )
        movieViewModel.deleteMovie.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.data)

                    if (removeMoviePos >= 0 && removeMoviePos < movieViewModel.movieDetailList.size) {
                        movieViewModel.movieDetailList.removeAt(removeMoviePos)
                        movieListAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("SelectGenreFragment", "Invalid delete position: $removeMoviePos")
                    }
                }
            }
        }


    }

    override fun onCancelClick() {
        deleteDialog.hideDialog()
    }


}