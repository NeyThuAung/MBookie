package com.example.mbookie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
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

    private val _saveMovie = MutableLiveData<UiState<String>>()
    val saveMovie : LiveData<UiState<String>>
        get() = _saveMovie

    fun saveMovie(movie : MovieDetail){
        _saveMovie.value = UiState.Loading
        movieUseCase.saveMovie(movie) {
            _saveMovie.value = it
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

    private val _saveCinema = MutableLiveData<UiState<String>>()
    val saveCinema : LiveData<UiState<String>>
        get() = _saveCinema

    fun saveCinema(cinema : Cinema){
        _saveCinema.value = UiState.Loading
        movieUseCase.saveCinema(cinema) {
            _saveCinema.value = it
        }
    }

    private val _saveSeat = MutableLiveData<UiState<String>>()
    val saveSeat : LiveData<UiState<String>>
        get() = _saveSeat

    fun saveSeat(seatList : ArrayList<Seat>){
        _saveSeat.value = UiState.Loading
        movieUseCase.saveSeat(seatList) {
            _saveSeat.value = it
        }
    }

}