package com.example.mbookie.data.usecase

import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
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

}