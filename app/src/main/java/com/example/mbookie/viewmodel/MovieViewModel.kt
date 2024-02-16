package com.example.mbookie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.repository.MovieRepository
import com.example.mbookie.data.usecase.MovieUseCase
import com.example.mbookie.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel(){

    var selectedGenreList = ArrayList<Genre>()
    var genreLst = ArrayList<Genre>()
    var genreId : String = ""
    var genreToEdit = Genre()

    private val _saveGenre = MutableLiveData<UiState<String>>()
    val saveGenre : LiveData<UiState<String>>
        get() = _saveGenre

    fun saveGenre(note : Genre){
        _saveGenre.value = UiState.Loading
        movieUseCase.saveGenre(note) {
            _saveGenre.value = it
        }
    }

    private val _genreList = MutableLiveData<UiState<List<Genre>>>()
    val genreList : LiveData<UiState<List<Genre>>>
        get() = _genreList

    fun getGenreList(){
        _genreList.value = UiState.Loading
        movieUseCase.getGenreList {
            _genreList.value = it
        }
    }

    private val _updateGenre = MutableLiveData<UiState<String>>()
    val updateGenre : LiveData<UiState<String>>
        get() = _updateGenre

    fun updateGenre(note : Genre){
        _updateGenre.value = UiState.Loading
        movieUseCase.updateGenre(note) {
            _updateGenre.value = it
        }
    }

    private val _deleteGenre = MutableLiveData<UiState<String>>()
    val deleteGenre : LiveData<UiState<String>>
        get() = _deleteGenre

    fun deleteGenre(genreId : String){
        _deleteGenre.value = UiState.Loading
        movieUseCase.deleteGenre(genreId) {
            _deleteGenre.value = it
        }
    }

}