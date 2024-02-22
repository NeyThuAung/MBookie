package com.example.mbookie.admin.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.MovieCategoryAdapter
import com.example.mbookie.admin.ui.adapter.TotalSeatAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.Seat
import com.example.mbookie.databinding.FragmentCreateCinemaBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.MovieCategoryData
import com.example.mbookie.util.TotalSeatData
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.util.totalSeatList
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.ArrayList


class CreateCinemaFragment : Fragment() {

    private lateinit var binding: FragmentCreateCinemaBinding

    private var selectedPos = 0
    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private var rows = 0
    private var columns = 10

    private var oldName = ""
    private var oldAddress = ""
    private var oldTotalSeat = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateCinemaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        if (movieViewModel.cinemaId.isNotEmpty()) {
            setEditLayout()
        }

        binding.tvSave.setOnClickListener {
            if (movieViewModel.cinemaId.isNotEmpty()) {
                updateCinema()
            } else
                createCinema()
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        bindMovieCategoryDropDown(totalSeatList)
        doAfterTextChange()

    }

    private fun setEditLayout() {
        binding.tvSelectGenreTitle.text = "Edit Cinema"

        binding.etCinema.setText(movieViewModel.editCinemaDetail.name)
        oldName = movieViewModel.editCinemaDetail.name.toString()

        binding.etAddress.setText(movieViewModel.editCinemaDetail.address)
        oldAddress = movieViewModel.editCinemaDetail.address.toString()

        binding.actvTotalSeat.setText(movieViewModel.editCinemaDetail.totalSeat.toString())
        oldTotalSeat = movieViewModel.editCinemaDetail.totalSeat!!

    }

    private fun createCinema() {
        movieViewModel.saveCinema(
            Cinema(
                name = binding.etCinema.text.toString(),
                totalSeat = binding.actvTotalSeat.text.toString().toInt(),
                address = binding.etAddress.text.toString()
            )
        )

        movieViewModel.saveCinema.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {
                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
                    saveSeat(state.data)
                    loadingDialog.hideDialog()
                }
            }
        }
    }

    private fun updateCinema() {
        movieViewModel.updateCinema(
            Cinema(
                id = movieViewModel.editCinemaDetail.id,
                name = binding.etCinema.text.toString(),
                totalSeat = binding.actvTotalSeat.text.toString().toInt(),
                address = binding.etAddress.text.toString()
            )
        )

        movieViewModel.updateCinema.observe(viewLifecycleOwner) { state ->
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

                    if (binding.actvTotalSeat.text.toString().toInt() != oldTotalSeat) {
                        deleteSeat(movieViewModel.editCinemaDetail.id.toString())
                    } else {
                        findNavController().navigateUp()
                    }

                }
            }
        }

    }

    private fun deleteSeat(cinemaId: String) {
        movieViewModel.deleteSeat(
            cinemaId
        )
        movieViewModel.deleteSeat.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {

                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {

                }

                is UiState.Success -> {

                    requireActivity().showToast(state.data)
                    saveSeat(cinemaId)
                }
            }
        }
    }

    private fun saveSeat(cinemaId: String) {

        val seatArrayList = createSeatArrayList(rows, columns, cinemaId)

        movieViewModel.saveSeat(
            seatArrayList
        )

        movieViewModel.saveSeat.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
//                    loadingDialog.hideDialog()
                    requireActivity().showToast(state.error.toString())
                }

                UiState.Loading -> {
//                    loadingDialog.showDialog()
                }

                is UiState.Success -> {
//                    loadingDialog.hideDialog()
                    requireActivity().showToast("Seat has been successfully created.")
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun generateSeatNumbers(rows: Int, columns: Int): List<String> {
        val seatNumbers = mutableListOf<String>()
        val rowsLabels = ('A'..'Z').take(rows)
        for (rowLabel in rowsLabels) {
            for (column in 1..columns) {
                seatNumbers.add("$rowLabel$column")
            }
        }
        return seatNumbers
    }

    private fun createSeatArrayList(rows: Int, columns: Int, cinemaId: String): ArrayList<Seat> {
        val seatNumbers = generateSeatNumbers(rows, columns)
        val seatArrayList = ArrayList<Seat>()
        for (i in 0 until seatNumbers.size) {
            val seatNumber = seatNumbers[i]
            val availableSeat = true // You can set the availability as needed
            seatArrayList.add(Seat("", seatNumber, availableSeat, cinemaId))
        }
        return seatArrayList
    }

    private fun doAfterTextChange() {
        binding.etCinema.doAfterTextChanged {
            binding.tvSave.checkRequirement()
        }
        binding.etAddress.doAfterTextChanged {
            binding.tvSave.checkRequirement()
        }
    }

    private fun bindMovieCategoryDropDown(totalSeatList: ArrayList<TotalSeatData>) {
        binding.actvTotalSeat.setDropDownBackgroundResource(R.color.white)
        val mAdapter =
            TotalSeatAdapter(
                requireContext(),
                R.layout.movie_category_item_card,
                selectedPos,
                totalSeatList
            )
        binding.actvTotalSeat.setAdapter(mAdapter)

        binding.actvTotalSeat.setOnItemClickListener { _, _, i, _ ->
            selectedPos = i
            if (totalSeatList[i].id != -1) {
//                productDetailViewModel.productTypeId = lst[i].id
                rows = totalSeatList[i].totalSeat.dropLast(1).toInt()
            } else {
//                productDetailViewModel.productTypeId = 0
            }

            binding.actvTotalSeat.setText(totalSeatList[i].totalSeat.toString())
            binding.tvSave.checkRequirement()
        }
    }

    private fun TextView.checkRequirement() {

        isEnabled = if (movieViewModel.cinemaId.isEmpty()) {
            binding.etCinema.text.toString().trim().isNotEmpty() &&
                    binding.actvTotalSeat.text.toString().trim()
                        .isNotEmpty() && (selectedPos != 0) &&
                    binding.etAddress.text.toString().trim().isNotEmpty()
        } else {
            binding.etCinema.text.toString().trim().isNotEmpty() &&
                    binding.actvTotalSeat.text.toString().trim()
                        .isNotEmpty() &&
                    binding.etAddress.text.toString().trim().isNotEmpty() &&
                    ((binding.etCinema.text.toString() != oldName) || (binding.actvTotalSeat.text.toString()
                        .toInt() != oldTotalSeat)
                            || (binding.etAddress.text.toString() != oldAddress))
        }

    }

}