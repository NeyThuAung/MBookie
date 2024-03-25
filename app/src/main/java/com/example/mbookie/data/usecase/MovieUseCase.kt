package com.example.mbookie.data.usecase

import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
import com.example.mbookie.data.model.Show
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.data.repository.MovieRepository
import com.example.mbookie.util.UiState
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun saveGenre(genre: Genre, result: (UiState<String>) -> Unit) {
        movieRepository.saveGenre(genre) { uiState ->
            result(uiState)
        }
    }

    fun getGenreList(result: (UiState<List<Genre>>) -> Unit) {
        movieRepository.getGenreList(result)
    }

    fun updateGenre(genre: Genre, result: (UiState<String>) -> Unit) {
        movieRepository.updateGenre(genre) { uiState ->
            result(uiState)
        }
    }

    fun deleteGenre(genreID: String, result: (UiState<String>) -> Unit) {
        movieRepository.deleteGenre(genreID) { uiState ->
            result(uiState)
        }
    }

    fun saveMovie(movie: MovieDetail, result: (UiState<String>) -> Unit) {
        movieRepository.saveMovie(movie) { uiState ->
            result(uiState)
        }
    }

    fun saveCinema(cinema: Cinema, result: (UiState<String>) -> Unit){
        movieRepository.saveCinema(cinema){uiState ->
            result(uiState)
        }
    }

    fun saveSeat(seatList : ArrayList<Seat>, result: (UiState<String>) -> Unit){
        movieRepository.saveSeat(seatList){uiState ->
            result(uiState)
        }
    }

    fun getMovieList(result: (UiState<List<MovieDetail>>) -> Unit) {
        movieRepository.getMovieList(result)
    }
    fun getCinemaList(result: (UiState<List<Cinema>>) -> Unit) {
        movieRepository.getCinemaList(result)
    }

    fun saveShowtime(showDateList : ArrayList<ShowDate>, movieId : String, cinemaId : String, result : (UiState<String>) -> Unit){
        movieRepository.saveShowTime(showDateList,movieId,cinemaId){uiState ->
            result(uiState)
        }
    }

    fun getShowList(movieId : String, cinemaId : String, result: (UiState<List<Show>>) -> Unit) {
        movieRepository.getShowList(movieId, cinemaId, result)
    }

    fun deleteShowTime(showId : String, result: (UiState<String>) -> Unit) {
        movieRepository.deleteShow(showId) { uiState ->
            result(uiState)
        }
    }
    fun updateMovieDetail(movieDetail: MovieDetail, result: (UiState<String>) -> Unit) {
        movieRepository.updateMovieDetail(movieDetail) { uiState ->
            result(uiState)
        }
    }

    fun getCinemaListForCinema(movieId : String, result: (UiState<List<Cinema>>) -> Unit) {
        movieRepository.getAvailableCinemaForMovieList(movieId, result)
    }

    fun updateCinema(cinema: Cinema, result: (UiState<String>) -> Unit) {
        movieRepository.updateCinema(cinema) { uiState ->
            result(uiState)
        }
    }

    fun deleteSeat(cinemaID: String, result: (UiState<String>) -> Unit) {
        movieRepository.deleteSeat(cinemaID) { uiState ->
            result(uiState)
        }
    }

    fun deleteCinema(cinemaID: String, result: (UiState<String>) -> Unit) {
        movieRepository.deleteCinema(cinemaID) { uiState ->
            result(uiState)
        }
    }
    fun deleteMovie(movieId : String, result: (UiState<String>) -> Unit) {
        movieRepository.deleteMovie(movieId) { uiState ->
            result(uiState)
        }
    }
    fun getGenreListWithSelectedIdLst(selectedGenreIdLst: ArrayList<String>,result: (UiState<List<Genre>>) -> Unit) {
        movieRepository.getGenreListWithIdList(selectedGenreIdLst,result)
    }

    fun getMovieListWithCategory(mCategory : Int,result: (UiState<List<MovieDetail>>) -> Unit) {
        movieRepository.getMovieListWithCategory(mCategory, result)
    }


}