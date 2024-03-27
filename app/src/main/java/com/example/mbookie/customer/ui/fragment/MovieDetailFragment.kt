package com.example.mbookie.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.AvailableCinemaAdapter
import com.example.mbookie.admin.ui.adapter.SelectCinemaAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.FragmentMovieDetailBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.CustomerViewModel
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest

class MovieDetailFragment : Fragment(),SelectCinemaAdapter.OnItemClickListener {

    private lateinit var binding : com.example.mbookie.databinding.FragmentMovieDetailBinding
    private val movieViewModel: MovieViewModel by activityViewModels()
    private val customerViewModel: CustomerViewModel by activityViewModels()
    private lateinit var selectCinemaAdapter : SelectCinemaAdapter

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private lateinit var movieDetail: MovieDetail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false


        getMovieDetail()
        getAvailableCinema()
        onClickEvent()

    }

    private fun getAvailableCinema() {
        lifecycleScope.launchWhenStarted {
            customerViewModel.getAvailableCinemaForMovie(customerViewModel.movieId).collectLatest { result->
                when(result){
                    is UiState.Failure -> {
                        loadingDialog.hideDialog()
                        Toast.makeText(
                            requireContext(),
                            result.error.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    UiState.Loading -> {
                        loadingDialog.showDialog()
                    }
                    is UiState.Success -> {
                        loadingDialog.hideDialog()
                        if (result.data.isNotEmpty()){
                            binding.recSelectCinemaList.isVisible = true
                            selectCinemaAdapter = SelectCinemaAdapter(result.data as ArrayList<Cinema>,this@MovieDetailFragment)
                            binding.recSelectCinemaList.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                adapter = selectCinemaAdapter
                            }
                        }else{
                            binding.recSelectCinemaList.isVisible = false
                        }
                        Log.d("ILKHKHKJH", "getAvailableCinema: ${result.data}")
                    }
                }
            }
        }
    }

    private fun onClickEvent() {
        binding.cvWatchTrailer.setOnClickListener{
            if (!movieDetail.mTrailerLink.isNullOrEmpty()) {
                customerViewModel.movieTrailerLink = movieDetail.mTrailerLink.toString()
                findNavController().navigate(R.id.action_movieDetailFragment_to_playTrailerFragment)
            } else
                requireContext().showToast("There is no trailer for this movie.")
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getMovieDetail() {
        lifecycleScope.launchWhenStarted {
            customerViewModel.getMovieDetail(customerViewModel.movieId).collectLatest { result->
                when(result){
                    is UiState.Failure -> {
                        loadingDialog.hideDialog()
                        Toast.makeText(
                            requireContext(),
                            result.error.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    UiState.Loading -> {
                        loadingDialog.showDialog()
                    }
                    is UiState.Success -> {
                        loadingDialog.hideDialog()

                        movieDetail = result.data
                        setUpDetails()
                    }
                }
            }
        }
    }

    private fun setUpDetails() {

        Glide.with(binding.root)
            .load(movieDetail.mPosterImg)
            .centerCrop()
            .into(binding.ivMoviePoster)

        binding.tvMovieTitle.text = movieDetail.mTitle
        binding.tvMovieDateDuration.text = movieDetail.mDuration +" . "+movieDetail.mReleaseDate

        binding.tvGenre.text = movieDetail.mGenre

        binding.llCensorship.isVisible = !movieDetail.mCensorship.isNullOrEmpty()
        binding.tvCensorship.text = movieDetail.mCensorship

        binding.tvLanguage.text = movieDetail.mLanguage

        binding.llDescription.isVisible = !movieDetail.mDescription.isNullOrEmpty()
        binding.tvDescription.text = movieDetail.mDescription
    }

    override fun onMovieRootClick(cinema: Cinema) {
        binding.mbContinueToBook.isEnabled = true
    }

}