package com.example.mbookie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
import com.example.mbookie.data.model.Show
import com.example.mbookie.data.model.ShowDate
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

    var selectedCinema = Cinema()
    var movieId = ""
    var editMovieDetail = MovieDetail()

    var showTimeList = arrayListOf<Show>()
    var manageCinemaList = arrayListOf<Cinema>()

    var cinemaId =""
    var editCinemaDetail = Cinema()

    var movieDetailList = arrayListOf<MovieDetail>()

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

    private val _movieList = MutableLiveData<UiState<List<MovieDetail>>>()
    val movieList : LiveData<UiState<List<MovieDetail>>>
        get() = _movieList
    fun getMovieList(){
        _movieList.value = UiState.Loading
        movieUseCase.getMovieList {
            _movieList.value = it
        }
    }

    private val _cinemaList = MutableLiveData<UiState<List<Cinema>>>()
    val cinemaList : LiveData<UiState<List<Cinema>>>
        get() = _cinemaList
    fun getCinemaList(){
        _cinemaList.value = UiState.Loading
        movieUseCase.getCinemaList {
            _cinemaList.value = it
        }
    }

    private val _saveShowtime = MutableLiveData<UiState<String>>()
    val saveShowtime : LiveData<UiState<String>>
        get() = _saveShowtime

    fun saveShowtime(showDateList : ArrayList<ShowDate>, movieId : String, cinemaId : String){
        _saveShowtime.value = UiState.Loading
        movieUseCase.saveShowtime(showDateList,movieId,cinemaId) {
            _saveShowtime.value = it
        }
    }

    private val _showList = MutableLiveData<UiState<List<Show>>>()
    val showList : LiveData<UiState<List<Show>>>
        get() = _showList
    fun getShowList(movieId : String, cinemaId : String){
        _showList.value = UiState.Loading
        movieUseCase.getShowList(movieId, cinemaId) {
            _showList.value = it
        }
    }

    private val _deleteShowTime = MutableLiveData<UiState<String>>()
    val deleteShowtime : LiveData<UiState<String>>
        get() = _deleteShowTime

    fun deleteShowtime(showId : String){
        _deleteShowTime.value = UiState.Loading
        movieUseCase.deleteShowTime(showId) {
            _deleteShowTime.value = it
        }
    }

    private val _updateMovie = MutableLiveData<UiState<String>>()
    val updateMovie : LiveData<UiState<String>>
        get() = _updateMovie

    fun updateMovie(movieDetail: MovieDetail){
        _updateMovie.value = UiState.Loading
        movieUseCase.updateMovieDetail(movieDetail) {
            _updateMovie.value = it
        }
    }

    private val _cinemaListForMovie = MutableLiveData<UiState<List<Cinema>>>()
    val cinemaListForMovie : LiveData<UiState<List<Cinema>>>
        get() = _cinemaListForMovie
    fun getCinemaListForMovie(movieId : String){
        _cinemaListForMovie.value = UiState.Loading
        movieUseCase.getCinemaListForCinema(movieId) {
            _cinemaListForMovie.value = it
        }
    }

    private val _updateCinema = MutableLiveData<UiState<String>>()
    val updateCinema : LiveData<UiState<String>>
        get() = _updateCinema

    fun updateCinema(cinema : Cinema){
        _updateCinema.value = UiState.Loading
        movieUseCase.updateCinema(cinema) {
            _updateCinema.value = it
        }
    }


    private val _deleteSeat = MutableLiveData<UiState<String>>()
    val deleteSeat : LiveData<UiState<String>>
        get() = _deleteSeat

    fun deleteSeat(cinemaId : String){
        _deleteSeat.value = UiState.Loading
        movieUseCase.deleteSeat(cinemaId) {
            _deleteSeat.value = it
        }
    }

    private val _deleteCinema = MutableLiveData<UiState<String>>()
    val deleteCinema : LiveData<UiState<String>>
        get() = _deleteCinema

    fun deleteCinema(cinemaId : String){
        _deleteCinema.value = UiState.Loading
        movieUseCase.deleteCinema(cinemaId) {
            _deleteCinema.value = it
        }
    }

    private val _deleteMovie = MutableLiveData<UiState<String>>()
    val deleteMovie : LiveData<UiState<String>>
        get() = _deleteMovie

    fun deleteMovie(movieId : String){
        _deleteMovie.value = UiState.Loading
        movieUseCase.deleteMovie(movieId) {
            _deleteMovie.value = it
        }
    }
}