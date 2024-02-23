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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.ManageCinemaAdapter
import com.example.mbookie.admin.ui.adapter.SelectCinemaAdapter
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.databinding.FragmentManageCinemaBinding
import com.example.mbookie.util.DeleteDialog
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ManageCinemaFragment : Fragment(), ManageCinemaAdapter.OnItemClickListener,
    DeleteDialog.AlertButtonListener {

    private lateinit var binding: FragmentManageCinemaBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private val deleteDialog: DeleteDialog by lazy {
        DeleteDialog(requireActivity())
    }

    private lateinit var manageCinemaAdapter: ManageCinemaAdapter

    private var deleteCinemaId = ""
    private var deletePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentManageCinemaBinding.inflate(layoutInflater, container, false)
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
                    movieViewModel.manageCinemaList.clear()
                    movieViewModel.manageCinemaList.addAll(state.data)

                    if (movieViewModel.manageCinemaList.isNotEmpty()){
                        binding.llNoCinemaFound.isVisible = false
                        binding.recCinemaList.isVisible = true
                        initRecycler()
                    }else{
                        binding.llNoCinemaFound.isVisible = true
                        binding.recCinemaList.isVisible = false
                    }


                }
            }
        }

    }

    private fun initRecycler() {
        manageCinemaAdapter = ManageCinemaAdapter(movieViewModel.manageCinemaList, this)
        binding.recCinemaList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = manageCinemaAdapter
        }
    }

    private fun onClickEvents() {

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.floatingActionAddCinema.setOnClickListener {
            movieViewModel.cinemaId = ""
            findNavController().navigate(R.id.action_manageCinemaFragment_to_createCinemaFragment)
        }

    }

    override fun onEditCinemaClick(cinema: Cinema) {
        movieViewModel.cinemaId = cinema.id.toString()
        movieViewModel.editCinemaDetail = cinema
        findNavController().navigate(R.id.action_manageCinemaFragment_to_createCinemaFragment)
    }

    override fun onDeleteCinemaClick(cinemaId: String, deletePos: Int) {
        deleteCinemaId = cinemaId
        deletePosition = deletePos
        deleteDialog.showDialog("Are you sure?", this, "Are you sure want to delete this cinema?")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPositiveClick() {
        movieViewModel.deleteCinema(
            deleteCinemaId
        )
        movieViewModel.deleteCinema.observe(viewLifecycleOwner) { state ->
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

        if (deletePosition >= 0 && deletePosition < movieViewModel.manageCinemaList.size) {
            movieViewModel.manageCinemaList.removeAt(deletePosition)
            manageCinemaAdapter.notifyDataSetChanged()
        } else {
            Log.e("SelectGenreFragment", "Invalid delete position: $deletePosition")
        }
    }

    override fun onCancelClick() {
        deleteDialog.hideDialog()
    }


}