package com.example.mbookie.admin.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mbookie.R
import com.example.mbookie.data.model.Genre
import com.example.mbookie.databinding.FragmentCreateGenreBinding
import com.example.mbookie.util.LoadingDialog
import com.example.mbookie.util.UiState
import com.example.mbookie.util.showToast
import com.example.mbookie.viewmodel.MovieViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date

class CreateGenreFragment : Fragment() {

    private lateinit var binding: FragmentCreateGenreBinding

    private val movieViewModel: MovieViewModel by activityViewModels()
    private var oldGenre = ""

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
        binding = FragmentCreateGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (movieViewModel.genreId.isNotEmpty()) {
            setEditLayout()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etGenre.doAfterTextChanged {
            binding.tvSave.checkRequirement()
        }

        binding.tvSave.setOnClickListener {
            if (movieViewModel.genreId.isNotEmpty()){
                updateGenre()
            }else{
                createGenre()
            }
        }

    }

    private fun updateGenre() {
        movieViewModel.updateGenre(
            Genre(
                id = movieViewModel.genreToEdit.id,
                genre = binding.etGenre.text.toString()
            )
        )

        movieViewModel.updateGenre.observe(viewLifecycleOwner) { state ->
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

    }

    private fun createGenre() {
        movieViewModel.saveGenre(
            Genre(
                id = "",
                genre = binding.etGenre.text.toString()
            )
        )

        movieViewModel.saveGenre.observe(viewLifecycleOwner) { state ->
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
    }

    private fun setEditLayout() {
        binding.tvSelectGenreTitle.text = "Edit Genre"

        binding.etGenre.setText(movieViewModel.genreToEdit.genre.toString())
        oldGenre = movieViewModel.genreToEdit.genre.toString()

        binding.tvSave.checkRequirement()

    }

    private fun TextView.checkRequirement() {
        isEnabled = if (movieViewModel.genreId.isNullOrEmpty()) {
            binding.etGenre.text.toString().trim().isNotEmpty()
        } else {
            binding.etGenre.text.toString().trim().isNotEmpty() &&
                    (binding.etGenre.text.toString() != oldGenre)
        }
    }


}