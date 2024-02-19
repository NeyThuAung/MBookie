package com.example.mbookie.admin.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import java.util.ArrayList


class CreateCinemaFragment : Fragment() {

    private lateinit var binding : FragmentCreateCinemaBinding

    private var selectedPos = 0
    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private var rows = 0
    private var columns = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateCinemaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSave.setOnClickListener {
            createCinema()
        }

        bindMovieCategoryDropDown(totalSeatList)
        doAfterTextChange()

    }

    private fun createCinema() {
        movieViewModel.saveCinema(
            Cinema(
                cName = binding.etCinema.text.toString(),
                cTotalSeat = binding.actvTotalSeat.text.toString().toInt(),
                cAddress = binding.etAddress.text.toString()
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

    private fun saveSeat(cinemaId: String) {

        val seatArrayList = createSeatArrayList(rows, columns, cinemaId)

        movieViewModel.saveSeat(
            seatArrayList
        )

        movieViewModel.saveSeat.observe(viewLifecycleOwner) { state ->
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
                    requireActivity().showToast("Movie has been successfully created.")
                    findNavController().popBackStack()
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

    private fun bindMovieCategoryDropDown(totalSeatList : ArrayList<TotalSeatData>) {
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
//        isEnabled = if (movieViewModel.genreId.isNullOrEmpty()) {
//            binding.etGenre.text.toString().trim().isNotEmpty()
//        } else {
//            binding.etGenre.text.toString().trim().isNotEmpty() &&
//                    (binding.etGenre.text.toString() != oldGenre)
//        }

        isEnabled = binding.etCinema.text.toString().trim().isNotEmpty() &&
                binding.actvTotalSeat.text.toString().trim().isNotEmpty() && (selectedPos != 0) &&
                binding.etAddress.text.toString().trim().isNotEmpty()

    }

}