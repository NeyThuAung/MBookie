package com.example.mbookie.admin.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbookie.R
import com.example.mbookie.admin.ui.adapter.GenreAdapter
import com.example.mbookie.data.model.Genre
import com.example.mbookie.databinding.FragmentSelectGenreBinding
import com.example.mbookie.util.DeleteDialog
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SelectGenreFragment : Fragment(), GenreAdapter.OnItemClickListener,
    DeleteDialog.AlertButtonListener {

    private lateinit var binding: FragmentSelectGenreBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private lateinit var genreAdapter: GenreAdapter
    private var deleteGenreId: String = ""
    private var deletePosition = 0
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(
            requireActivity()
        )
    }

    private val deleteDialog: DeleteDialog by lazy {
        DeleteDialog(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelectGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation form Vetail Home Page Activity
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false

        onClickEvent()

        movieViewModel.getGenreList()
        movieViewModel.genreList.observe(viewLifecycleOwner) { state ->
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

                    movieViewModel.genreLst.clear()
                    movieViewModel.genreLst.addAll(state.data as ArrayList<Genre>)
                    retrieveSelectedGenre(movieViewModel.genreLst)

                    genreAdapter = GenreAdapter(movieViewModel.genreLst, this)
                    binding.recSelectGenre.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = genreAdapter
                    }

                }
            }
        }

    }

    private fun retrieveSelectedGenre(genreList: ArrayList<Genre>) {

        for (i in movieViewModel.selectedGenreList) {
            outer@ for (j in genreList) {
                if (i.id == j.id) {
                    j.isSelected = true
                    onCheckCategoryState(j)
                    break@outer
                }
            }
        }

    }

    private fun onClickEvent() {

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.floatingActionAddGenre.setOnClickListener {
            movieViewModel.genreId = ""
            findNavController().navigate(R.id.action_selectGenreFragment_to_createGenreFragment)
        }

        binding.tvSave.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun TextView.checkRequirement() {
        isEnabled = movieViewModel.selectedGenreList.isNotEmpty()
    }

    override fun onEditClick(genreInfo: Genre) {
        movieViewModel.genreId = genreInfo.id.toString()
        movieViewModel.genreToEdit = genreInfo
        findNavController().navigate(R.id.action_selectGenreFragment_to_createGenreFragment)
    }

    override fun onDeleteClick(genreId: String, deletePos: Int) {
        Log.d("HKLJHKJH", "onDeleteClick: $deletePos")
        deleteGenreId = genreId
        deletePosition = deletePos
        deleteDialog.showDialog("Are you sure?", this, "Are you sure want to delete this genre?")
    }

    override fun onCheckCategoryState(checkedGenre: Genre) {

        if (!movieViewModel.selectedGenreList.contains(checkedGenre)) {
            movieViewModel.selectedGenreList.add(checkedGenre)
        }
        binding.tvSave.checkRequirement()

    }

    override fun onUncheckCategoryState(genreId: String) {
        val lst = movieViewModel.selectedGenreList.filter {
            it.id != genreId
        }
        if (lst.isNotEmpty()) {
            movieViewModel.selectedGenreList = lst as ArrayList<Genre>
        } else
            movieViewModel.selectedGenreList.clear()

        binding.tvSave.checkRequirement()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPositiveClick() {
        movieViewModel.deleteGenre(
            deleteGenreId
        )
        movieViewModel.deleteGenre.observe(viewLifecycleOwner) { state ->
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
                    // Remove the item from the genreList
                    if (deletePosition >= 0 && deletePosition < movieViewModel.genreLst.size) {
                        movieViewModel.genreLst.removeAt(deletePosition)
                        genreAdapter.notifyDataSetChanged() // Update the adapter
                    } else {
                        Log.e("SelectGenreFragment", "Invalid delete position: $deletePosition")
                    }
                }
            }
        }
    }

    override fun onCancelClick() {
        deleteDialog.hideDialog()
    }

}