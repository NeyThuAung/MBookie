package com.example.mbookie.admin.ui.movie

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.SelectCinemaAdapter
import com.example.mbookie.admin.ui.adapter.ShowtimeAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Show
import com.example.mbookie.databinding.FragmentCinemaShowTimesBinding
import com.example.mbookie.util.DeleteDialog
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel

class CinemaShowTimesFragment : Fragment(), ShowtimeAdapter.OnItemClickListener,
    DeleteDialog.AlertButtonListener {

    private lateinit var binding: FragmentCinemaShowTimesBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private lateinit var showtimeAdapter: ShowtimeAdapter

    private val deleteDialog: DeleteDialog by lazy {
        DeleteDialog(requireActivity())
    }

    private var deletePosition = -1
    private var deleteShowId = ""

    override fun onResume() {
        super.onResume()

        getShowList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCinemaShowTimesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCinemaName.text = movieViewModel.selectedCinema.name
        binding.tvLocation.text = movieViewModel.selectedCinema.address

        onClickListener()

    }

    private fun getShowList() {
        movieViewModel.getShowList(
            movieViewModel.movieId,
            movieViewModel.selectedCinema.id.toString()
        )
        movieViewModel.showList.observe(viewLifecycleOwner) { state ->
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
                    movieViewModel.showTimeList.clear()
                    movieViewModel.showTimeList.addAll(state.data as ArrayList<Show>)
                    if (movieViewModel.showTimeList.isNullOrEmpty()) {
                        binding.tvShowTimeTitle.isVisible = false
                        binding.recSelectedShowtime.isVisible = false
                        binding.tvSave.isEnabled = false
                        binding.llNoShowtimeFound.isVisible = true
                    } else {
                        binding.tvShowTimeTitle.isVisible = true
                        binding.recSelectedShowtime.isVisible = true
                        binding.tvSave.isEnabled = true
                        binding.llNoShowtimeFound.isVisible = false
                        initRecycler()
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        showtimeAdapter = ShowtimeAdapter(movieViewModel.showTimeList, this)
        binding.recSelectedShowtime.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                requireContext(),
                2,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = showtimeAdapter
        }
    }

    private fun onClickListener() {
        binding.cvAddShowtime.setOnClickListener {
            findNavController().navigate(R.id.action_cinemaShowTimesFragment_to_setUpShowTimeFragment)
        }

        binding.tvSave.setOnClickListener {
            findNavController().navigate(R.id.action_cinemaShowTimesFragment_to_movieListFragment)
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onRemoveShowClick(showId: String, removePos: Int) {
        deleteShowId = showId
        deletePosition = removePos
        deleteDialog.showDialog("Are you sure?", this, "Are you sure want to delete this showtime?")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPositiveClick() {

        movieViewModel.deleteShowtime(
            deleteShowId
        )
        movieViewModel.deleteShowtime.observe(viewLifecycleOwner) { state ->
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
                }
            }
        }

        if (deletePosition >= 0 && deletePosition < movieViewModel.showTimeList.size) {
            movieViewModel.showTimeList.removeAt(deletePosition)
            showtimeAdapter.notifyDataSetChanged()
        } else {
            Log.e("SelectGenreFragment", "Invalid delete position: $deletePosition")
        }
    }

    override fun onCancelClick() {
        deleteDialog.hideDialog()
    }

}