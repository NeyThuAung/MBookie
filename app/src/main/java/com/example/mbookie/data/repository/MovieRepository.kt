package com.example.mbookie.data.repository

import android.net.Uri
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
import com.example.mbookie.data.model.Show
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.util.UiState

interface MovieRepository {
    fun saveGenre(genre : Genre, result : (UiState<String>) -> Unit)
    fun getGenreList(result : (UiState<List<Genre>>) -> Unit)
    fun updateGenre(genre : Genre, result : (UiState<String>) -> Unit)
    fun deleteGenre(genreId : String, result : (UiState<String>) -> Unit)
    fun saveMovie(movie : MovieDetail, result : (UiState<String>) -> Unit)

    fun saveCinema(cinema: Cinema, result : (UiState<String>) -> Unit)

    fun saveSeat(seatList : ArrayList<Seat>, result : (UiState<String>) -> Unit)

    fun getMovieList(result : (UiState<List<MovieDetail>>) -> Unit)
    fun getCinemaList(result : (UiState<List<Cinema>>) -> Unit)
    fun saveShowTime(showDateList : ArrayList<ShowDate>,mId : String, cId : String, result : (UiState<String>) -> Unit)
    fun getShowList(movieId : String, cinemaId : String, result : (UiState<List<Show>>) -> Unit)

    fun deleteShow(showId : String, result : (UiState<String>) -> Unit)

    fun updateMovieDetail(movieDetail : MovieDetail, result : (UiState<String>) -> Unit)

    fun getAvailableCinemaForMovieList(movieId : String, result : (UiState<List<Cinema>>) -> Unit)

    fun updateCinema(cinema : Cinema, result : (UiState<String>) -> Unit)
    fun deleteSeat(cinemaId : String, result : (UiState<String>) -> Unit)

    fun deleteCinema(cinemaId : String, result : (UiState<String>) -> Unit)

    fun   deleteMovie(movieId : String, result : (UiState<String>) -> Unit)

    fun getGenreListWithIdList(selectedGenreIdLst : ArrayList<String>,result : (UiState<List<Genre>>) -> Unit)
}